import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeft, Volume2, VolumeX, Monitor, Sun, Moon } from 'lucide-react';

const Settings: React.FC = () => {
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [musicVolume, setMusicVolume] = useState(70);
  const [effectsVolume, setEffectsVolume] = useState(80);
  const [graphicsQuality, setGraphicsQuality] = useState<'low' | 'medium' | 'high'>('high');
  const [shadowsEnabled, setShadowsEnabled] = useState(true);
  const [antiAliasing, setAntiAliasing] = useState(true);
  const [theme, setTheme] = useState<'light' | 'dark'>('dark');

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-900 to-slate-700 flex items-center justify-center">
      <div className="w-full max-w-2xl mx-auto p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <Link
            to="/"
            className="flex items-center space-x-2 text-gray-300 hover:text-white transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
            <span>Back to Menu</span>
          </Link>
          <h1 className="text-3xl font-bold text-white">Settings</h1>
          <div className="w-20"></div>
        </div>

        {/* Settings Panel */}
        <div className="bg-slate-800/90 backdrop-blur-sm rounded-lg p-6 shadow-lg space-y-6">
          {/* Audio Settings */}
          <div className="space-y-4">
            <h2 className="text-xl font-semibold text-white flex items-center">
              <Volume2 className="w-5 h-5 mr-2" />
              Audio Settings
            </h2>
            
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <label className="text-gray-300">Sound Effects</label>
                <button
                  onClick={() => setSoundEnabled(!soundEnabled)}
                  className={`p-2 rounded-lg transition-colors ${
                    soundEnabled ? 'bg-green-600 text-white' : 'bg-slate-600 text-gray-400'
                  }`}
                >
                  {soundEnabled ? <Volume2 className="w-4 h-4" /> : <VolumeX className="w-4 h-4" />}
                </button>
              </div>
              
              <div className="space-y-2">
                <label className="text-gray-300">Music Volume</label>
                <div className="flex items-center space-x-3">
                  <input
                    type="range"
                    min="0"
                    max="100"
                    value={musicVolume}
                    onChange={(e) => setMusicVolume(Number(e.target.value))}
                    className="flex-1 h-2 bg-slate-600 rounded-lg appearance-none cursor-pointer"
                  />
                  <span className="text-gray-300 w-12 text-right">{musicVolume}%</span>
                </div>
              </div>
              
              <div className="space-y-2">
                <label className="text-gray-300">Effects Volume</label>
                <div className="flex items-center space-x-3">
                  <input
                    type="range"
                    min="0"
                    max="100"
                    value={effectsVolume}
                    onChange={(e) => setEffectsVolume(Number(e.target.value))}
                    className="flex-1 h-2 bg-slate-600 rounded-lg appearance-none cursor-pointer"
                  />
                  <span className="text-gray-300 w-12 text-right">{effectsVolume}%</span>
                </div>
              </div>
            </div>
          </div>

          {/* Graphics Settings */}
          <div className="space-y-4">
            <h2 className="text-xl font-semibold text-white flex items-center">
              <Monitor className="w-5 h-5 mr-2" />
              Graphics Settings
            </h2>
            
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <label className="text-gray-300">Graphics Quality</label>
                <select
                  value={graphicsQuality}
                  onChange={(e) => setGraphicsQuality(e.target.value as 'low' | 'medium' | 'high')}
                  className="bg-slate-700 text-white px-3 py-2 rounded-lg border border-slate-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="low">Low</option>
                  <option value="medium">Medium</option>
                  <option value="high">High</option>
                </select>
              </div>
              
              <div className="flex items-center justify-between">
                <label className="text-gray-300">Shadows</label>
                <button
                  onClick={() => setShadowsEnabled(!shadowsEnabled)}
                  className={`px-4 py-2 rounded-lg transition-colors ${
                    shadowsEnabled 
                      ? 'bg-green-600 text-white' 
                      : 'bg-slate-600 text-gray-400'
                  }`}
                >
                  {shadowsEnabled ? 'Enabled' : 'Disabled'}
                </button>
              </div>
              
              <div className="flex items-center justify-between">
                <label className="text-gray-300">Anti-Aliasing</label>
                <button
                  onClick={() => setAntiAliasing(!antiAliasing)}
                  className={`px-4 py-2 rounded-lg transition-colors ${
                    antiAliasing 
                      ? 'bg-green-600 text-white' 
                      : 'bg-slate-600 text-gray-400'
                  }`}
                >
                  {antiAliasing ? 'Enabled' : 'Disabled'}
                </button>
              </div>
            </div>
          </div>

          {/* Interface Settings */}
          <div className="space-y-4">
            <h2 className="text-xl font-semibold text-white flex items-center">
              {theme === 'dark' ? <Moon className="w-5 h-5 mr-2" /> : <Sun className="w-5 h-5 mr-2" />}
              Interface Settings
            </h2>
            
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <label className="text-gray-300">Theme</label>
                <button
                  onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}
                  className={`px-4 py-2 rounded-lg transition-colors flex items-center space-x-2 ${
                    theme === 'dark' 
                      ? 'bg-slate-700 text-white' 
                      : 'bg-yellow-600 text-white'
                  }`}
                >
                  {theme === 'dark' ? <Moon className="w-4 h-4" /> : <Sun className="w-4 h-4" />}
                  <span>{theme === 'dark' ? 'Dark' : 'Light'}</span>
                </button>
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex space-x-4 pt-6">
            <button
              className="flex-1 bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-semibold transition-colors"
            >
              Apply Settings
            </button>
            <button
              className="flex-1 bg-slate-600 hover:bg-slate-700 text-white px-6 py-3 rounded-lg font-semibold transition-colors"
            >
              Reset to Defaults
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Settings;