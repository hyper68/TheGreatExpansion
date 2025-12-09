import React, { useRef, useEffect, useState } from 'react';
import { Canvas, useFrame, useThree } from '@react-three/fiber';
import { OrbitControls, Sky, Environment } from '@react-three/drei';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';
import Terrain from './Terrain';
import BuildingSystem from './BuildingSystem';
import PisteSystem from './PisteSystem';
import LiftSystem from './LiftSystem';
import SkierNPCSystem from './SkierNPCSystem';
import CameraController from './CameraController';

const GameScene: React.FC = () => {
  const { cameraPosition, cameraTarget, updateCamera } = useGameStore();
  
  return (
    <div className="w-full h-screen bg-gradient-to-b from-blue-400 to-blue-600">
      <Canvas
        camera={{
          position: [50, 50, 50],
          fov: 60,
          near: 0.1,
          far: 1000
        }}
        shadows
        gl={{ antialias: true, alpha: false }}
      >
        {/* Lighting */}
        <ambientLight intensity={0.4} />
        <directionalLight
          position={[100, 100, 50]}
          intensity={1}
          castShadow
          shadow-mapSize-width={2048}
          shadow-mapSize-height={2048}
          shadow-camera-far={200}
          shadow-camera-left={-100}
          shadow-camera-right={100}
          shadow-camera-top={100}
          shadow-camera-bottom={-100}
        />
        
        {/* Sky and Environment */}
        <Sky
          distance={450000}
          sunPosition={[0, 1, 0]}
          inclination={0}
          azimuth={0.25}
        />
        
        {/* Fog for atmospheric effect */}
        <fog attach="fog" args={['#87CEEB', 100, 500]} />
        
        {/* Camera Controller */}
        <CameraController />
        
        {/* Game Systems */}
        <Terrain />
        <BuildingSystem />
        <PisteSystem />
        <LiftSystem />
        <SkierNPCSystem />
        
        {/* Grid Helper for reference */}
        <gridHelper args={[200, 50, '#ffffff', '#cccccc']} position={[0, 0.1, 0]} />
      </Canvas>
    </div>
  );
};

export default GameScene;