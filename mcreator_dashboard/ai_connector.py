"""
AI Connector - lightweight heuristics to suggest probable connections between elements.

This module does NOT call external services. It uses simple textual similarity
and presence heuristics to propose likely links that weren't explicitly
found by deterministic parsing.
"""
from typing import Dict, List, Tuple
from difflib import SequenceMatcher
import re
import os
import math

# Optional OpenAI embeddings support. The key must be provided via the
# OPENAI_API_KEY environment variable. We do NOT accept keys via code
# or chat. If available, embeddings will be used to improve text similarity.
OPENAI_AVAILABLE = False
try:
    if os.getenv('OPENAI_API_KEY'):
        import openai
        openai.api_key = os.getenv('OPENAI_API_KEY')
        OPENAI_AVAILABLE = True
except Exception:
    OPENAI_AVAILABLE = False


def _text_from_element(el) -> str:
    parts = []
    try:
        d = getattr(el, 'raw_data', {}) or {}
        # flatten some definition text
        defn = d.get('definition', {}) if isinstance(d, dict) else {}
        for k, v in defn.items():
            try:
                parts.append(str(v))
            except Exception:
                pass
    except Exception:
        pass

    # names and file_name
    try:
        parts.append(getattr(el, 'name', ''))
    except Exception:
        pass
    try:
        parts.append(getattr(el, 'file_name', ''))
    except Exception:
        pass

    txt = ' '.join([p for p in parts if p])
    # normalize
    txt = txt.lower()
    txt = re.sub(r'[^a-z0-9 ]+', ' ', txt)
    txt = ' '.join(txt.split())
    return txt


def _similarity(a: str, b: str) -> float:
    if not a or not b:
        return 0.0
    return SequenceMatcher(None, a, b).ratio()


def _cosine(a: List[float], b: List[float]) -> float:
    if not a or not b:
        return 0.0
    dot = sum(x * y for x, y in zip(a, b))
    na = math.sqrt(sum(x * x for x in a))
    nb = math.sqrt(sum(y * y for y in b))
    if na == 0 or nb == 0:
        return 0.0
    return dot / (na * nb)


def _get_embeddings(texts: List[str]) -> List[List[float]]:
    """Return embeddings using OpenAI if available, otherwise empty list."""
    if not OPENAI_AVAILABLE:
        return []

    try:
        # use a small embedding model; this will incur API costs
        resp = openai.Embedding.create(model='text-embedding-3-small', input=texts)
        return [r['embedding'] for r in resp['data']]
    except Exception:
        return []


def find_connections(elements: Dict[str, object], relationships: Dict[str, Dict], threshold: float = 0.62, max_suggestions: int = 300) -> List[Dict]:
    """
    Scan elements and propose likely connections not present in relationships.

    Returns list of suggestions: {source, target, score, reason}
    """
    names = list(elements.keys())
    texts = {name: _text_from_element(elements[name]) for name in names}

    # If OpenAI is available, compute embeddings for each element's text
    embeddings = []
    if OPENAI_AVAILABLE:
        try:
            emb_texts = [texts.get(n, '') for n in names]
            embeddings = _get_embeddings(emb_texts)
        except Exception:
            embeddings = []

    suggestions = []

    # Build set of existing links to avoid duplicates
    existing = set()
    for name, rel in (relationships or {}).items():
        for p in rel.get('produces', []):
            existing.add((name, p.get('produces', '') if isinstance(p, dict) else ''))
        for c in rel.get('consumed_by', []):
            existing.add((name, c.get('produces', '') if isinstance(c, dict) else ''))
        for r in rel.get('requires', []):
            existing.add((r, name))

    n = len(names)
    for i in range(n):
        a = names[i]
        txt_a = texts.get(a, '')
        for j in range(n):
            if i == j:
                continue
            b = names[j]
            txt_b = texts.get(b, '')

            # skip if already related roughly (direct match)
            if (a, b) in existing or (b, a) in existing:
                continue

            # name similarity and text similarity
            name_sim = _similarity(a.lower(), b.lower())
            text_sim = _similarity(txt_a, txt_b)
            # if embeddings available, use cosine similarity instead of text_sim
            if embeddings and len(embeddings) == n:
                try:
                    emb_sim = _cosine(embeddings[i], embeddings[j])
                    # blend embedding similarity with textual similarity
                    text_sim = max(text_sim, emb_sim)
                except Exception:
                    pass

            # presence heuristic: if a's name appears in b's text, strong signal
            presence = 1.0 if (re.search(r'\b' + re.escape(a.split()[0].lower()) + r'\b', txt_b) if a else False) else 0.0

            score = max(name_sim * 0.9, text_sim, presence)

            if score >= threshold:
                suggestions.append({
                    'source': a,
                    'target': b,
                    'score': round(score, 3),
                    'reason': 'name' if name_sim >= text_sim else ('presence' if presence > 0 else 'text')
                })

    # sort and limit
    suggestions.sort(key=lambda x: x['score'], reverse=True)
    return suggestions[:max_suggestions]
