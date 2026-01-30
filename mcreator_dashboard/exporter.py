"""
Spreadsheet Exporter
Export mod elements to Excel or CSV formats
"""
import pandas as pd
from typing import Dict, List
from pathlib import Path

from parser import Element


def export_to_excel(elements: Dict[str, Element], output_path: str):
    """
    Export elements to Excel with multiple sheets by type

    Args:
        elements: Dictionary of parsed elements
        output_path: Path to output Excel file
    """
    # Group elements by type
    elements_by_type = {}
    for element in elements.values():
        element_type = element.element_type
        if element_type not in elements_by_type:
            elements_by_type[element_type] = []
        elements_by_type[element_type].append(element)

    # Create Excel writer
    with pd.ExcelWriter(output_path, engine='openpyxl') as writer:
        # Overview sheet with all elements
        all_data = [element.to_dict() for element in elements.values()]
        df_all = pd.DataFrame(all_data)
        df_all = df_all.sort_values('Name')
        df_all.to_excel(writer, sheet_name='All Elements', index=False)

        # Apply formatting
        worksheet = writer.sheets['All Elements']
        _apply_excel_formatting(worksheet, df_all)

        # Individual sheets for each type
        for element_type, type_elements in sorted(elements_by_type.items()):
            # Sanitize sheet name (Excel has 31 char limit)
            sheet_name = element_type.replace('_', ' ').title()[:31]

            type_data = [element.to_dict() for element in type_elements]
            df_type = pd.DataFrame(type_data)
            df_type = df_type.sort_values('Name')
            df_type.to_excel(writer, sheet_name=sheet_name, index=False)

            worksheet = writer.sheets[sheet_name]
            _apply_excel_formatting(worksheet, df_type)

        # Statistics sheet
        stats_data = _generate_statistics(elements, elements_by_type)
        df_stats = pd.DataFrame(stats_data)
        df_stats.to_excel(writer, sheet_name='Statistics', index=False)

    print(f"[OK] Exported {len(elements)} elements to {output_path}")


def export_to_csv(elements: Dict[str, Element], output_path: str):
    """
    Export elements to a single CSV file

    Args:
        elements: Dictionary of parsed elements
        output_path: Path to output CSV file
    """
    all_data = [element.to_dict() for element in elements.values()]
    df = pd.DataFrame(all_data)
    df = df.sort_values(['Type', 'Name'])
    df.to_csv(output_path, index=False, encoding='utf-8')

    print(f"[OK] Exported {len(elements)} elements to {output_path}")


def _apply_excel_formatting(worksheet, dataframe):
    """Apply formatting to Excel worksheet"""
    from openpyxl.styles import Font

    # Auto-adjust column widths
    for idx, col in enumerate(dataframe.columns):
        max_length = max(
            dataframe[col].astype(str).apply(len).max(),
            len(col)
        )
        # Set max width to 50
        adjusted_width = min(max_length + 2, 50)
        worksheet.column_dimensions[chr(65 + idx)].width = adjusted_width

    # Make header bold
    for cell in worksheet[1]:
        cell.font = Font(bold=True)

    # Freeze first row
    worksheet.freeze_panes = 'A2'


def _generate_statistics(elements: Dict[str, Element], elements_by_type: Dict[str, List[Element]]) -> List[Dict]:
    """Generate statistics for the statistics sheet"""
    stats = []

    # Type counts
    stats.append({'Statistic': 'Total Elements', 'Value': len(elements)})
    stats.append({'Statistic': '', 'Value': ''})  # Empty row

    stats.append({'Statistic': 'Elements by Type', 'Value': ''})
    for element_type, type_elements in sorted(elements_by_type.items(), key=lambda x: len(x[1]), reverse=True):
        stats.append({
            'Statistic': f'  {element_type}',
            'Value': len(type_elements)
        })

    stats.append({'Statistic': '', 'Value': ''})  # Empty row

    # Recipe statistics
    recipe_count = len([e for e in elements.values() if e.element_type == 'recipe'])
    stats.append({'Statistic': 'Total Recipes', 'Value': recipe_count})

    # Find items with most recipes
    items_with_recipes = [(e.name, len(e.recipes_using)) for e in elements.values() if e.recipes_using]
    items_with_recipes.sort(key=lambda x: x[1], reverse=True)

    stats.append({'Statistic': '', 'Value': ''})  # Empty row
    stats.append({'Statistic': 'Most Used in Recipes', 'Value': ''})
    for name, count in items_with_recipes[:10]:
        stats.append({'Statistic': f'  {name}', 'Value': count})

    # Find items that produce most outputs
    items_with_outputs = [(e.name, len(e.recipe_outputs)) for e in elements.values() if e.recipe_outputs]
    items_with_outputs.sort(key=lambda x: x[1], reverse=True)

    stats.append({'Statistic': '', 'Value': ''})  # Empty row
    stats.append({'Statistic': 'Most Recipe Outputs', 'Value': ''})
    for name, count in items_with_outputs[:10]:
        stats.append({'Statistic': f'  {name}', 'Value': count})

    # Orphaned items (no recipes)
    orphaned = [e.name for e in elements.values()
                if not e.recipes_using and not e.recipe_outputs
                and e.element_type not in ['recipe', 'procedure', 'advancement']]

    stats.append({'Statistic': '', 'Value': ''})  # Empty row
    stats.append({'Statistic': 'Orphaned Items (no recipes)', 'Value': len(orphaned)})

    return stats


if __name__ == '__main__':
    # Test the exporter
    import sys
    from parser import MCreatorParser

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        elements_path = '../elements'

    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    # Export to Excel
    export_to_excel(elements, 'mod_elements.xlsx')

    # Export to CSV
    export_to_csv(elements, 'mod_elements.csv')
