@echo off
echo ================================================================
echo   MCreator Mod Dashboard - Installation
echo   Version 2.0 - Modern UI Edition
echo ================================================================
echo.

echo Installing Python dependencies...
echo.
echo This will install:
echo   - pandas (data processing)
echo   - openpyxl (Excel export)
echo   - networkx (graph analysis)
echo   - pyvis (interactive flowcharts)
echo   - matplotlib (visualizations)
echo   - ttkbootstrap (modern UI)
echo   - pillow (image support)
echo.

pip install -r requirements.txt

echo.
echo ================================================================
echo   Installation Complete!
echo ================================================================
echo.
echo You can now launch the dashboard with:
echo   python main.py
echo.
echo Or simply double-click:
echo   run_dashboard.bat
echo.
echo To run tests:
echo   python test_modern_ui.py
echo.
echo For help, see:
echo   README.md
echo   QUICKSTART.md
echo   STATUS.md
echo.
pause
