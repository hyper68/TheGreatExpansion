import React from 'react';
import { Link } from 'react-router-dom';
import { Mountain, Play, Settings, FileText } from 'lucide-react';

const Home: React.FC = () => {
  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-900 via-blue-700 to-blue-500 flex items-center justify-center">
      {/* Animated background */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-b from-transparent to-blue-900/50"></div>
        {/* Snow particles */}
        <div className="absolute inset-0">
          {Array.from({ length: 50 }).map((_, i) => (
            <div
              key={i}
              className="absolute w-2 h-2 bg-white rounded-full opacity-60 animate-pulse"
              style={{
                left: `${Math.random() * 100}%`,
                top: `${Math.random() * 100}%`,
                animationDelay: `${Math.random() * 3}s`,
                animationDuration: `${3 + Math.random() * 2}s`
              }}
            />
          ))}
        </div>
      </div>
      
      {/* Main content */}
      <div className="relative z-10 text-center max-w-2xl mx-auto px-4">
        {/* Logo and title */}
        <div className="mb-8">
          <div className="flex items-center justify-center mb-4">
            <Mountain className="w-16 h-16 text-white mr-4" />
            <h1 className="text-6xl font-bold text-white tracking-tight">
              Ski Resort
              <span className="block text-5xl text-blue-200">Tycoon</span>
            </h1>
          </div>
          <p className="text-xl text-blue-100 mb-8">
            Build and manage your dream ski resort in stunning 3D. Design ski runs, 
            construct lifts, and satisfy thousands of skiers in this ultimate winter simulation.
          </p>
        </div>
        
        {/* Menu buttons */}
        <div className="space-y-4">
          <Link
            to="/game"
            className="group w-full max-w-sm mx-auto bg-green-600 hover:bg-green-700 text-white px-8 py-4 rounded-lg font-semibold text-lg flex items-center justify-center space-x-3 transition-all duration-200 transform hover:scale-105 shadow-lg"
          >
            <Play className="w-6 h-6" />
            <span>Start New Game</span>
          </Link>
          
          <button
            className="group w-full max-w-sm mx-auto bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 rounded-lg font-semibold text-lg flex items-center justify-center space-x-3 transition-all duration-200 transform hover:scale-105 shadow-lg"
          >
            <FileText className="w-6 h-6" />
            <span>Load Game</span>
          </button>
          
          <Link
            to="/settings"
            className="group w-full max-w-sm mx-auto bg-slate-600 hover:bg-slate-700 text-white px-8 py-4 rounded-lg font-semibold text-lg flex items-center justify-center space-x-3 transition-all duration-200 transform hover:scale-105 shadow-lg"
          >
            <Settings className="w-6 h-6" />
            <span>Settings</span>
          </Link>
        </div>
        
        {/* Features preview */}
        <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6 text-blue-100">
          <div className="bg-blue-800/30 backdrop-blur-sm rounded-lg p-4">
            <Mountain className="w-8 h-8 mx-auto mb-2" />
            <h3 className="font-semibold mb-1">3D Terrain</h3>
            <p className="text-sm opacity-90">Realistic mountain landscapes with dynamic snow</p>
          </div>
          <div className="bg-blue-800/30 backdrop-blur-sm rounded-lg p-4">
            <div className="w-8 h-8 mx-auto mb-2 flex items-center justify-center">
              <div className="w-6 h-6 bg-blue-300 rounded-full"></div>
            </div>
            <h3 className="font-semibold mb-1">Smart AI</h3>
            <p className="text-sm opacity-90">Intelligent skiers with realistic behavior</p>
          </div>
          <div className="bg-blue-800/30 backdrop-blur-sm rounded-lg p-4">
            <div className="w-8 h-8 mx-auto mb-2 flex items-center justify-center">
              <div className="w-6 h-4 bg-green-400 rounded"></div>
            </div>
            <h3 className="font-semibold mb-1">Economy</h3>
            <p className="text-sm opacity-90">Deep financial management and strategy</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;