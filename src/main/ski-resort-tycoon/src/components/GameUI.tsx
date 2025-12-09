import React, { useState, useEffect } from 'react';
import { useGameStore } from '../stores/gameStore';
import { BUILDING_TYPES } from '../config/buildings';
import { 
  DollarSign, 
  Users, 
  Play, 
  Pause, 
  FastForward,
  Hammer,
  Settings,
  BarChart3,
  Home,
  Mountain
} from 'lucide-react';

const GameUI: React.FC = () => {
  const { 
    economy, 
    isPaused, 
    gameSpeed, 
    isBuildMode, 
    setGameSpeed, 
    setBuildMode,
    currentResort,
    activeSkiers,
    selectedBuildingType,
    setSelectedBuilding
  } = useGameStore();
  
  const [showManagementPanel, setShowManagementPanel] = useState(false);
  const [selectedBuildingCategory, setSelectedBuildingCategory] = useState<'lift' | 'building' | 'facility'>('lift');
  
  // Auto-update economy every second
  useEffect(() => {
    const interval = setInterval(() => {
      useGameStore.getState().updateEconomy();
    }, 1000);
    
    return () => clearInterval(interval);
  }, []);
  
  // Spawn skiers periodically
  useEffect(() => {
    const interval = setInterval(() => {
      if (activeSkiers.length < currentResort!.guestCapacity && Math.random() < 0.3) {
        useGameStore.getState().addSkier();
      }
    }, 2000);
    
    return () => clearInterval(interval);
  }, [activeSkiers.length, currentResort?.guestCapacity]);
  
  const formatMoney = (amount: number): string => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(amount);
  };
  
  const getSpeedIcon = () => {
    if (gameSpeed === 0) return <Pause className="w-4 h-4" />;
    if (gameSpeed === 1) return <Play className="w-4 h-4" />;
    return <FastForward className="w-4 h-4" />;
  };
  
  const getSpeedLabel = () => {
    if (gameSpeed === 0) return 'Paused';
    if (gameSpeed === 1) return 'Normal';
    return 'Fast';
  };
  
  return (
    <div className="absolute inset-0 pointer-events-none">
      {/* Top HUD Bar */}
      <div className="absolute top-0 left-0 right-0 p-4">
        <div className="bg-slate-800/90 backdrop-blur-sm rounded-lg p-4 shadow-lg">
          <div className="flex items-center justify-between">
            {/* Money and Stats */}
            <div className="flex items-center space-x-6">
              <div className="flex items-center space-x-2 text-green-400">
                <DollarSign className="w-5 h-5" />
                <span className="font-bold text-lg">{formatMoney(economy.currentMoney)}</span>
              </div>
              
              <div className="flex items-center space-x-2 text-blue-400">
                <Users className="w-5 h-5" />
                <span className="font-semibold">{activeSkiers.length} / {currentResort?.guestCapacity}</span>
              </div>
              
              <div className="flex items-center space-x-2 text-yellow-400">
                <BarChart3 className="w-5 h-5" />
                <span className="font-semibold">{Math.round(economy.guestSatisfaction)}%</span>
              </div>
            </div>
            
            {/* Control Buttons */}
            <div className="flex items-center space-x-2">
              <button
                onClick={() => setGameSpeed(gameSpeed === 0 ? 1 : 0)}
                className="pointer-events-auto bg-slate-700 hover:bg-slate-600 text-white px-3 py-2 rounded-md flex items-center space-x-2 transition-colors"
              >
                {getSpeedIcon()}
                <span className="text-sm">{getSpeedLabel()}</span>
              </button>
              
              <button
                onClick={() => setGameSpeed(gameSpeed === 2 ? 1 : 2)}
                className="pointer-events-auto bg-slate-700 hover:bg-slate-600 text-white px-3 py-2 rounded-md transition-colors"
              >
                <FastForward className="w-4 h-4" />
              </button>
              
              <button
                onClick={() => setBuildMode(!isBuildMode)}
                className={`pointer-events-auto px-3 py-2 rounded-md flex items-center space-x-2 transition-colors ${
                  isBuildMode 
                    ? 'bg-blue-600 hover:bg-blue-700 text-white' 
                    : 'bg-slate-700 hover:bg-slate-600 text-white'
                }`}
              >
                <Hammer className="w-4 h-4" />
                <span className="text-sm">Build</span>
              </button>
              
              <button
                onClick={() => setShowManagementPanel(!showManagementPanel)}
                className="pointer-events-auto bg-slate-700 hover:bg-slate-600 text-white p-2 rounded-md transition-colors"
              >
                <BarChart3 className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
      
      {/* Build Mode Panel */}
      {isBuildMode && (
        <div className="absolute top-20 right-4 w-80 pointer-events-auto">
          <div className="bg-slate-800/90 backdrop-blur-sm rounded-lg p-4 shadow-lg">
            <h3 className="text-white font-bold text-lg mb-4 flex items-center">
              <Hammer className="w-5 h-5 mr-2" />
              Build Mode
            </h3>
            
            {/* Building Categories */}
            <div className="flex space-x-2 mb-4">
              {(['lift', 'building', 'facility'] as const).map((category) => (
                <button
                  key={category}
                  onClick={() => setSelectedBuildingCategory(category)}
                  className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                    selectedBuildingCategory === category
                      ? 'bg-blue-600 text-white'
                      : 'bg-slate-700 text-gray-300 hover:bg-slate-600'
                  }`}
                >
                  {category.charAt(0).toUpperCase() + category.slice(1)}
                </button>
              ))}
            </div>
            
            {/* Building Options */}
            <div className="space-y-2 max-h-96 overflow-y-auto">
              {BUILDING_TYPES
                .filter(type => type.category === selectedBuildingCategory)
                .map((buildingType) => (
                  <button
                    key={buildingType.id}
                    onClick={() => setSelectedBuilding(
                      selectedBuildingType?.id === buildingType.id ? null : buildingType
                    )}
                    className={`w-full p-3 rounded-lg text-left transition-colors ${
                      selectedBuildingType?.id === buildingType.id
                        ? 'bg-blue-600 text-white'
                        : 'bg-slate-700 text-gray-300 hover:bg-slate-600'
                    }`}
                  >
                    <div className="flex justify-between items-center">
                      <div>
                        <div className="font-medium">{buildingType.name}</div>
                        <div className="text-sm opacity-75">
                          ${buildingType.cost.toLocaleString()}
                        </div>
                      </div>
                      <div className="text-xs opacity-75">
                        ${buildingType.maintenanceCost}/day
                      </div>
                    </div>
                  </button>
                ))}
            </div>
            
            {/* Instructions */}
            <div className="mt-4 p-3 bg-slate-700 rounded-lg">
              <p className="text-gray-300 text-sm">
                {selectedBuildingType ? (
                  <>Click on terrain to place {selectedBuildingType.name}. Right-click to cancel.</>
                ) : (
                  <>Select a building type to start placing.</>
                )}
              </p>
            </div>
          </div>
        </div>
      )}
      
      {/* Management Panel */}
      {showManagementPanel && (
        <div className="absolute bottom-4 left-4 right-4 pointer-events-auto">
          <div className="bg-slate-800/90 backdrop-blur-sm rounded-lg p-6 shadow-lg">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-white font-bold text-lg flex items-center">
                <BarChart3 className="w-5 h-5 mr-2" />
                Resort Management
              </h3>
              <button
                onClick={() => setShowManagementPanel(false)}
                className="text-gray-400 hover:text-white transition-colors"
              >
                ×
              </button>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {/* Financial Overview */}
              <div className="bg-slate-700 rounded-lg p-4">
                <h4 className="text-white font-semibold mb-3">Financial Overview</h4>
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between text-green-400">
                    <span>Daily Revenue:</span>
                    <span>{formatMoney(economy.dailyRevenue)}</span>
                  </div>
                  <div className="flex justify-between text-red-400">
                    <span>Daily Expenses:</span>
                    <span>{formatMoney(economy.dailyExpenses)}</span>
                  </div>
                  <div className="flex justify-between text-blue-400 pt-2 border-t border-slate-600">
                    <span>Net Daily:</span>
                    <span>{formatMoney(economy.dailyRevenue - economy.dailyExpenses)}</span>
                  </div>
                </div>
              </div>
              
              {/* Guest Statistics */}
              <div className="bg-slate-700 rounded-lg p-4">
                <h4 className="text-white font-semibold mb-3">Guest Statistics</h4>
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between text-blue-400">
                    <span>Current Guests:</span>
                    <span>{activeSkiers.length}</span>
                  </div>
                  <div className="flex justify-between text-yellow-400">
                    <span>Satisfaction:</span>
                    <span>{Math.round(economy.guestSatisfaction)}%</span>
                  </div>
                  <div className="flex justify-between text-green-400">
                    <span>Total Served:</span>
                    <span>{economy.totalSkiers}</span>
                  </div>
                </div>
              </div>
              
              {/* Resort Information */}
              <div className="bg-slate-700 rounded-lg p-4">
                <h4 className="text-white font-semibold mb-3">Resort Info</h4>
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between text-white">
                    <span>Resort:</span>
                    <span>{currentResort?.name}</span>
                  </div>
                  <div className="flex justify-between text-purple-400">
                    <span>Reputation:</span>
                    <span>{currentResort?.reputation}/100</span>
                  </div>
                  <div className="flex justify-between text-orange-400">
                    <span>Capacity:</span>
                    <span>{currentResort?.guestCapacity}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
      
      {/* Bottom Status Bar */}
      <div className="absolute bottom-4 left-4 right-4 flex justify-center">
        <div className="bg-slate-800/90 backdrop-blur-sm rounded-lg px-4 py-2 shadow-lg">
          <div className="flex items-center space-x-4 text-sm text-gray-300">
            <div className="flex items-center space-x-2">
              <Mountain className="w-4 h-4" />
              <span>Alpine Valley Resort</span>
            </div>
            <div className="w-px h-4 bg-gray-600"></div>
            <span>{isBuildMode ? 'Build Mode' : 'Normal Mode'}</span>
            <div className="w-px h-4 bg-gray-600"></div>
            <span>{getSpeedLabel()}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GameUI;