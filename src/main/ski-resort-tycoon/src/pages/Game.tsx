import React from 'react';
import Game2D from '../components/Game2D';
import GameUI from '../components/GameUI';

const Game: React.FC = () => {
  return (
    <div className="relative w-full h-screen overflow-hidden bg-gradient-to-b from-blue-400 to-blue-600">
      {/* 2D Game Scene */}
      <Game2D />
      
      {/* UI Overlay */}
      <GameUI />
    </div>
  );
};

export default Game;