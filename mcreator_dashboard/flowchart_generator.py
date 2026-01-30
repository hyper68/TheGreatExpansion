"""
Interactive Flowchart Generator
Create interactive HTML flowcharts showing relationships between mod elements
"""
import networkx as nx
from pyvis.network import Network
from typing import Dict, Optional, Set, List
from pathlib import Path

from parser import Element, RecipeElement
from relationship_analyzer import RelationshipAnalyzer


class FlowchartGenerator:
    """Generate interactive flowcharts of mod element relationships"""

    def __init__(self, elements: Dict[str, Element], relationships: Optional[Dict] = None):
        self.elements = elements
        # relationships is expected to be a dict mapping element name -> relationship info
        self.relationships = relationships or {}
        self.graph = nx.DiGraph()
        self._build_graph()

    def _build_graph(self):
        """Build the directed graph from elements"""
        print("Building dependency graph...")

        # Add all elements as nodes
        for name, element in self.elements.items():
            node_color = self._get_node_color(element.element_type)
            node_shape = self._get_node_shape(element.element_type)
            # Fetch additional metadata from relationships if available
            rel = self.relationships.get(name, {}) if isinstance(self.relationships, dict) else {}

            self.graph.add_node(
                name,
                type=element.element_type,
                color=node_color,
                shape=node_shape,
                title=self._get_node_tooltip(element, rel),
                tier=rel.get('tier', 0),
                complexity=rel.get('complexity', 0),
                crafted_in=rel.get('crafted_in', [])
            )

        # Add edges for relationships
        for name, element in self.elements.items():
            # Recipe dependencies
            if isinstance(element, RecipeElement):
                # Inputs -> Recipe
                for input_item in element.inputs:
                    if input_item in self.graph:
                        self.graph.add_edge(input_item, name,
                                          relationship='recipe_input',
                                          color='#999999',
                                          title='Used in recipe')

                # Recipe -> Output
                if element.output in self.graph:
                    self.graph.add_edge(name, element.output,
                                      relationship='recipe_output',
                                      color='#4CAF50',
                                      title='Produces',
                                      width=2)

            # Generic dependencies
            for dep in element.dependencies:
                if dep in self.graph:
                    self.graph.add_edge(name, dep,
                                      relationship='dependency',
                                      color='#2196F3',
                                      title='Depends on',
                                      dashes=True)

        # Add relationship-driven edges (requires, crafted_in, dropped_by)
        if isinstance(self.relationships, dict):
            for item, rel in self.relationships.items():
                # Requires: inputs required for this item
                for req in rel.get('requires', []):
                    if req in self.graph and item in self.graph:
                        self.graph.add_edge(req, item,
                                            relationship='requires',
                                            color='#FF5722',
                                            title='Required to craft')

                # Crafted in: machines that make this item
                for machine in rel.get('crafted_in', []):
                    # Add machine node if missing
                    if machine not in self.graph:
                        self.graph.add_node(machine, type='machine', color='#795548', shape='box', title=f'Machine: {machine}')
                    self.graph.add_edge(machine, item,
                                        relationship='crafted_in',
                                        color='#009688',
                                        title=f'Crafted in {machine}',
                                        width=2)

                # Dropped by: mobs or blocks that drop this
                for drop in rel.get('dropped_by', []):
                    # drop may be dict entries like {'mob': name} or {'block': name}
                    mob = None
                    if isinstance(drop, dict):
                        mob = drop.get('mob') or drop.get('block')
                    else:
                        mob = drop
                    if mob and mob in self.graph and item in self.graph:
                        self.graph.add_edge(mob, item,
                                            relationship='dropped_by',
                                            color='#B71C1C',
                                            title='Drops')

                # AI-suggested edges (visual hints)
                for s in rel.get('ai_suggested_in', []):
                    src = s.get('source')
                    if src and src in self.graph and item in self.graph:
                        self.graph.add_edge(src, item,
                                            relationship='ai_suggested',
                                            color='#FFC107',
                                            title=f"AI-suggested ({s.get('score')})",
                                            dashes=True)

        print(f"Graph built: {len(self.graph.nodes)} nodes, {len(self.graph.edges)} edges")

    def generate_interactive_graph(self, output_path: str,
                                   focus_element: Optional[str] = None,
                                   depth: int = 3):
        """
        Generate an interactive HTML visualization

        Args:
            output_path: Path to save the HTML file
            focus_element: Optional element to focus on (shows only connected nodes)
            depth: How many levels deep to show from focus element
        """
        # Create subgraph if focusing on specific element
        if focus_element and focus_element in self.graph:
            print(f"Focusing on element: {focus_element} (depth={depth})")
            graph_to_visualize = self._create_focused_subgraph(focus_element, depth)
        else:
            graph_to_visualize = self.graph

        # Create Pyvis network
        net = Network(
            height='900px',
            width='100%',
            bgcolor='#1e1e1e',
            font_color='white',
            directed=True
        )

        # Configure physics for better layout
        net.set_options("""
        {
          "nodes": {
            "font": {
              "size": 14,
              "face": "arial"
            },
            "borderWidth": 2,
            "borderWidthSelected": 4
          },
          "edges": {
            "arrows": {
              "to": {
                "enabled": true,
                "scaleFactor": 0.5
              }
            },
            "smooth": {
              "enabled": true,
              "type": "continuous"
            }
          },
          "physics": {
            "barnesHut": {
              "gravitationalConstant": -30000,
              "centralGravity": 0.3,
              "springLength": 150,
              "springConstant": 0.04,
              "damping": 0.09,
              "avoidOverlap": 0.5
            },
            "maxVelocity": 50,
            "minVelocity": 0.1,
            "stabilization": {
              "enabled": true,
              "iterations": 1000,
              "updateInterval": 25
            }
          }
        }
        """)

        # Add nodes
        for node, data in graph_to_visualize.nodes(data=True):
            net.add_node(
                node,
                label=node,
                color=data.get('color', '#97C2FC'),
                shape=data.get('shape', 'dot'),
                title=data.get('title', node)
            )

        # Add edges
        for source, target, data in graph_to_visualize.edges(data=True):
            net.add_edge(
                source,
                target,
                color=data.get('color', '#848484'),
                title=data.get('title', ''),
                width=data.get('width', 1),
                dashes=data.get('dashes', False)
            )

        # Add title and info
        title = "MCreator Mod Flowchart - The Great Expansion"
        if focus_element:
            title += f" (Focused on: {focus_element})"

        net.heading = title

        # Save to HTML
        net.save_graph(output_path)

        # Add custom CSS and legend
        self._add_custom_html(output_path, focus_element)

        print(f"[OK] Interactive flowchart saved to {output_path}")

    def _create_focused_subgraph(self, focus_element: str, depth: int) -> nx.DiGraph:
        """Create a subgraph focused on a specific element"""
        # Find all nodes within depth from focus element
        nodes_to_include = {focus_element}

        # Traverse forward (dependencies)
        for d in range(depth):
            new_nodes = set()
            for node in list(nodes_to_include):
                # Outgoing edges (what this depends on)
                new_nodes.update(self.graph.successors(node))
                # Incoming edges (what depends on this)
                new_nodes.update(self.graph.predecessors(node))
            nodes_to_include.update(new_nodes)

        # Create subgraph
        subgraph = self.graph.subgraph(nodes_to_include).copy()
        print(f"Subgraph: {len(subgraph.nodes)} nodes, {len(subgraph.edges)} edges")
        return subgraph

    def _get_node_color(self, element_type: str) -> str:
        """Get color for node based on element type"""
        colors = {
            'block': '#8BC34A',      # Green
            'item': '#2196F3',       # Blue
            'recipe': '#FF9800',     # Orange
            'tool': '#9C27B0',       # Purple
            'armor': '#E91E63',      # Pink
            'mob': '#F44336',        # Red
            'livingentity': '#F44336',  # Red
            'dimension': '#00BCD4',  # Cyan
            'potion': '#673AB7',     # Deep Purple
            'procedure': '#607D8B',  # Blue Grey
            'biome': '#4CAF50',      # Light Green
        }
        return colors.get(element_type, '#9E9E9E')  # Default grey

    def _get_node_shape(self, element_type: str) -> str:
        """Get shape for node based on element type"""
        shapes = {
            'block': 'box',
            'item': 'ellipse',
            'recipe': 'diamond',
            'tool': 'star',
            'armor': 'triangle',
            'mob': 'dot',
            'livingentity': 'dot',
            'dimension': 'square',
            'potion': 'hexagon',
            'procedure': 'database',
        }
        return shapes.get(element_type, 'dot')

    def _get_node_tooltip(self, element: Element, rel: Dict = None) -> str:
        """Generate tooltip text for node, enriched with relationship metadata"""
        rel = rel or {}
        tooltip = f"<b>{element.name}</b><br>"
        tooltip += f"Type: {element.element_type}<br>"

        # Relationship-provided metadata
        tier = rel.get('tier')
        complexity = rel.get('complexity')
        crafted_in = rel.get('crafted_in', [])

        if tier is not None:
            tooltip += f"Tier: {tier}<br>"
        if complexity is not None:
            tooltip += f"Complexity: {complexity}<br>"
        if crafted_in:
            tooltip += f"Crafted In: {', '.join(crafted_in)}<br>"

        if element.dependencies:
            tooltip += f"Dependencies: {len(element.dependencies)}<br>"

        if element.recipes_using:
            tooltip += f"Used in {len(element.recipes_using)} recipes<br>"

        if element.recipe_outputs:
            tooltip += f"Produces {len(element.recipe_outputs)} outputs<br>"

        # Add type-specific info
        if hasattr(element, 'recipe_type'):
            tooltip += f"Recipe Type: {element.recipe_type}<br>"
            if element.output:
                tooltip += f"Output: {element.output} x{element.output_count}<br>"

        if hasattr(element, 'hardness'):
            tooltip += f"Hardness: {element.hardness}<br>"

        if hasattr(element, 'health'):
            tooltip += f"Health: {element.health}<br>"

        # Show some relationship samples
        reqs = rel.get('requires', [])
        if reqs:
            tooltip += f"Requires: {', '.join(reqs[:5])}" + ("..." if len(reqs) > 5 else "") + "<br>"

        dropped_by = rel.get('dropped_by', [])
        if dropped_by:
            # Format dropped_by entries
            drops = []
            for d in dropped_by[:5]:
                if isinstance(d, dict):
                    drops.append(d.get('mob') or d.get('block') or str(d))
                else:
                    drops.append(str(d))
            tooltip += f"Dropped By: {', '.join(drops)}" + ("..." if len(dropped_by) > 5 else "") + "<br>"

        return tooltip

    def _add_custom_html(self, html_path: str, focus_element: Optional[str]):
        """Add custom HTML, CSS, and legend to the generated file"""
        with open(html_path, 'r', encoding='utf-8') as f:
            html = f.read()

        # Add legend
        legend_html = """
        <div style="position: absolute; top: 60px; right: 10px; background: rgba(30,30,30,0.9);
                    padding: 15px; border-radius: 8px; color: white; font-family: Arial; z-index: 1000;
                    border: 2px solid #444;">
            <h3 style="margin-top: 0;">Legend</h3>
            <div style="margin: 5px 0;">
                <span style="color: #8BC34A;">●</span> Block &nbsp;&nbsp;
                <span style="color: #2196F3;">●</span> Item &nbsp;&nbsp;
                <span style="color: #FF9800;">●</span> Recipe
            </div>
            <div style="margin: 5px 0;">
                <span style="color: #9C27B0;">●</span> Tool &nbsp;&nbsp;
                <span style="color: #E91E63;">●</span> Armor &nbsp;&nbsp;
                <span style="color: #F44336;">●</span> Mob
            </div>
            <div style="margin: 5px 0;">
                <span style="color: #00BCD4;">●</span> Dimension &nbsp;&nbsp;
                <span style="color: #673AB7;">●</span> Potion
            </div>
            <hr style="border-color: #444;">
            <div style="margin: 5px 0; font-size: 12px;">
                <span style="color: #4CAF50;">→</span> Produces<br>
                <span style="color: #999999;">→</span> Recipe Input<br>
                <span style="color: #2196F3;">⇢</span> Depends On
            </div>
        </div>
        """

        # Add controls info
        controls_html = """
        <div style="position: absolute; bottom: 10px; left: 10px; background: rgba(30,30,30,0.9);
                    padding: 10px; border-radius: 8px; color: white; font-family: Arial; font-size: 12px;
                    border: 2px solid #444;">
            <b>Controls:</b> Drag to pan • Scroll to zoom • Click node for details • Drag nodes to rearrange
        </div>
        """

        # Insert before closing body tag
        # Add AI controls: toggle AI edges, suggest new connections, accept/reject and export
        ai_controls_html = """
        <div style="position: absolute; top: 10px; right: 10px; background: rgba(30,30,30,0.95); padding: 12px; border-radius: 8px; color: white; font-family: Arial; z-index:1000; border:2px solid #444; width:260px;">
            <div style="margin-bottom:6px;"><label style="font-size:13px;"><input id="toggleAiEdges" type="checkbox" checked style="margin-right:8px;"> Show AI-suggested edges</label></div>
            <div style="margin-bottom:6px;"><label style="font-size:13px;">Similarity threshold: <span id="simVal">0.6</span></label>
                <input id="simThreshold" type="range" min="0" max="1" step="0.01" value="0.6" style="width:100%;"></div>
            <div style="margin-bottom:6px;"><button id="suggestBtn" style="width:100%;padding:6px;">Suggest Connections</button></div>
            <div style="display:flex;gap:6px;"><button id="acceptAllBtn" style="flex:1;padding:6px;">Accept All</button><button id="exportBtn" style="flex:1;padding:6px;">Export CSV</button></div>
            <div style="margin-top:8px; max-height:220px; overflow:auto; font-size:12px;" id="aiSuggestionsList"></div>
        </div>

        <script>
        // Simple token Jaccard similarity for quick in-browser matching
        function tokens(s){ return (s||'').toLowerCase().replace(/[^a-z0-9 ]+/g,' ').split(/\s+/).filter(Boolean); }
        function jaccard(a,b){ var A=new Set(tokens(a)); var B=new Set(tokens(b)); if(A.size===0||B.size===0) return 0; var inter=0; A.forEach(x=>{ if(B.has(x)) inter++; }); var uni = new Set([...A,...B]).size; return inter/uni; }

        function setAiEdgesVisible(visible){
            try{
                var edges = network.body.data.edges.get();
                for(var i=0;i<edges.length;i++){ var e=edges[i]; var title = e.title||''; if(title.indexOf('AI-suggested')!==-1){ network.body.data.edges.update({id:e.id, hidden:!visible}); } }
            }catch(err){console.warn('AI toggle error',err)}
        }

        function findExistingPair(a,b){ var edges = network.body.data.edges.get(); for(var i=0;i<edges.length;i++){ var e=edges[i]; if((e.from==a && e.to==b) || (e.from==b && e.to==a)) return true; } return false; }

        function suggestConnections(){
            var nodes = network.body.data.nodes.get();
            var threshold = parseFloat(document.getElementById('simThreshold').value);
            var suggestions = [];
            for(var i=0;i<nodes.length;i++){
                for(var j=0;j<nodes.length;j++){
                    if(i===j) continue;
                    var a = nodes[i]; var b = nodes[j];
                    if(findExistingPair(a.id,b.id)) continue;
                    var score = jaccard(a.label||a.id, b.label||b.id);
                    if(score>=threshold){ suggestions.push({source:a.id,target:b.id,score:score}); }
                }
            }
            suggestions.sort((x,y)=>y.score-x.score);
            renderSuggestions(suggestions);
        }

        function renderSuggestions(suggestions){
            var list = document.getElementById('aiSuggestionsList'); list.innerHTML=''; window._currentSuggestions = suggestions;
            if(suggestions.length===0){ list.innerHTML='<div style="color:#ccc">No suggestions</div>'; return; }
            suggestions.forEach(function(s, idx){
                var div = document.createElement('div'); div.style.padding='6px'; div.style.borderBottom='1px solid #333'; div.style.display='flex'; div.style.justifyContent='space-between';
                var left = document.createElement('div'); left.innerHTML = '<b>'+s.source+'</b> → <b>'+s.target+'</b> <span style="color:#bbb">('+s.score.toFixed(2)+')</span>';
                var btn = document.createElement('button'); btn.textContent='Accept'; btn.style.marginLeft='8px'; btn.onclick = function(){ acceptSuggestion(s); btn.disabled=true; };
                div.appendChild(left); div.appendChild(btn); list.appendChild(div);
            });
        }

        function acceptSuggestion(s){
            // Add a permanent edge
            try{
                network.body.data.edges.add({from: s.source, to: s.target, title: 'AI-suggested accepted ('+s.score.toFixed(3)+')', color:'#FF9800', dashes:false, width:2});
            }catch(err){ console.warn('accept edge failed', err); }
        }

        function acceptAll(){ if(window._currentSuggestions){ window._currentSuggestions.forEach(s=>{ if(!findExistingPair(s.source,s.target)) acceptSuggestion(s); }); } }

        function exportCSV(){ var rows = window._currentSuggestions || []; var csv = 'source,target,score\n' + rows.map(r=>[r.source,r.target,r.score.toFixed(3)].join(',')).join('\n'); var blob = new Blob([csv],{type:'text/csv'}); var url = URL.createObjectURL(blob); var a=document.createElement('a'); a.href=url; a.download='ai_suggestions.csv'; document.body.appendChild(a); a.click(); a.remove(); URL.revokeObjectURL(url); }

        document.addEventListener('DOMContentLoaded', function(){
            var cb = document.getElementById('toggleAiEdges'); if(cb){ cb.addEventListener('change', function(){ setAiEdgesVisible(cb.checked); }); setTimeout(function(){ setAiEdgesVisible(cb.checked); }, 400); }
            document.getElementById('suggestBtn').addEventListener('click', function(){ suggestConnections(); });
            document.getElementById('acceptAllBtn').addEventListener('click', function(){ acceptAll(); });
            document.getElementById('exportBtn').addEventListener('click', function(){ exportCSV(); });
            var sim = document.getElementById('simThreshold'); var simVal = document.getElementById('simVal'); sim.addEventListener('input', function(){ simVal.textContent = sim.value; });
        });
        </script>
        """

        html = html.replace('</body>', f'{ai_controls_html}{legend_html}{controls_html}</body>')

        with open(html_path, 'w', encoding='utf-8') as f:
            f.write(html)

    def generate_recipe_chain(self, item_name: str, output_path: str):
        """
        Generate a flowchart showing the full recipe chain for a specific item

        Args:
            item_name: Name of the item to trace
            output_path: Path to save the HTML file
        """
        if item_name not in self.graph:
            print(f"Error: Item '{item_name}' not found in graph")
            return

        print(f"Tracing recipe chain for: {item_name}")

        # Find all nodes that lead to this item (recursive)
        chain_nodes = self._find_recipe_chain(item_name)

        # Create subgraph
        subgraph = self.graph.subgraph(chain_nodes).copy()

        # Generate visualization
        self.generate_interactive_graph(output_path, focus_element=item_name)

    def _find_recipe_chain(self, item_name: str, visited: Optional[Set[str]] = None) -> Set[str]:
        """Recursively find all items in the recipe chain"""
        if visited is None:
            visited = set()

        if item_name in visited or item_name not in self.graph:
            return visited

        visited.add(item_name)

        # Find all predecessors (items that lead to this)
        for predecessor in self.graph.predecessors(item_name):
            self._find_recipe_chain(predecessor, visited)

        return visited

    def get_crafting_tree(self, item_name: str) -> Dict:
        """
        Get a hierarchical crafting tree for an item

        Returns a dictionary representing the crafting tree
        """
        if item_name not in self.elements:
            return {}

        element = self.elements[item_name]
        tree = {
            'name': item_name,
            'type': element.element_type,
            'ingredients': []
        }

        # Find recipes that produce this item
        for recipe_name in element.recipe_outputs:
            if recipe_name in self.elements:
                recipe = self.elements[recipe_name]
                if isinstance(recipe, RecipeElement):
                    recipe_node = {
                        'recipe': recipe_name,
                        'type': recipe.recipe_type,
                        'inputs': []
                    }

                    # Recursively get trees for inputs
                    for input_item in recipe.inputs:
                        input_tree = self.get_crafting_tree(input_item)
                        recipe_node['inputs'].append(input_tree)

                    tree['ingredients'].append(recipe_node)

        return tree


def generate_simple_flowchart(elements: Dict[str, Element], output_path: str):
    """
    Simple function to generate a flowchart from elements

    Args:
        elements: Dictionary of parsed elements
        output_path: Path to save HTML file
    """
    generator = FlowchartGenerator(elements)
    generator.generate_interactive_graph(output_path)


if __name__ == '__main__':
    # Test the flowchart generator
    import sys
    from parser import MCreatorParser
    from relationship_analyzer import RelationshipAnalyzer

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        elements_path = '../elements'

    # Parse elements
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    # Build relationships and pass to generator so flowcharts include new info
    analyzer = RelationshipAnalyzer(elements)
    relationships = analyzer.relationships

    # Generate flowchart
    print("\n=== Generating Interactive Flowchart ===")
    generator = FlowchartGenerator(elements, relationships=relationships)

    # Full graph
    output_path = Path(elements_path).parent / 'mcreator_dashboard' / 'data' / 'flowchart.html'
    generator.generate_interactive_graph(str(output_path))

    print(f"\n[OK] Open {output_path} in a web browser to view the interactive flowchart!")

    # Example: Generate focused view on a specific item
    if len(sys.argv) > 2:
        focus_item = sys.argv[2]
        focused_output = Path(elements_path).parent / 'mcreator_dashboard' / 'data' / f'flowchart_{focus_item}.html'
        generator.generate_interactive_graph(str(focused_output), focus_element=focus_item, depth=2)
        print(f"[OK] Focused flowchart saved to {focused_output}")
