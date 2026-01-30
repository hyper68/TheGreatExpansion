Quick launcher
----------------

To create a Desktop and Start Menu shortcut that launches the dashboard, run the PowerShell helper from the `mcreator_dashboard` folder:

```powershell
cd c:\Users\bayne\Documents\TheGreatExpansion\mcreator_dashboard
powershell -ExecutionPolicy Bypass -File .\create_windows_shortcut.ps1
```

The script will create a shortcut named "MCreator Dashboard" on your Desktop and in the Start Menu Programs folder and assign the hotkey `Ctrl+Alt+D` by default.

If you want a different hotkey or name, pass parameters:

```powershell
powershell -ExecutionPolicy Bypass -File .\create_windows_shortcut.ps1 -ShortcutName "TGE Dashboard" -Hotkey "CTRL+ALT+T"
```

Notes:
- The script points the shortcut to `run_dashboard.bat`, which in turn runs `python main.py --elements-path "../elements"`.
- If PowerShell blocks script execution, use the `-ExecutionPolicy Bypass` flag as shown above.
