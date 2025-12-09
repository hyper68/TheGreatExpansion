import React, { useMemo, useRef, useEffect } from 'react';
import { useFrame } from '@react-three/fiber';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';

interface TerrainProps {
  width?: number;
  height?: number;
  resolution?: number;
}

const Terrain: React.FC<TerrainProps> = ({ 
  width = 200, 
  height = 200, 
  resolution = 50 
}) => {
  const meshRef = useRef<THREE.Mesh>(null);
  const { setTerrainData } = useGameStore();
  
  // Generate terrain using Perlin noise
  const terrainGeometry = useMemo(() => {
    const geometry = new THREE.PlaneGeometry(width, height, resolution, resolution);
    const vertices = geometry.attributes.position.array as Float32Array;
    
    // Simple noise function for terrain generation
    const noise = (x: number, y: number): number => {
      const scale = 0.02;
      return Math.sin(x * scale) * Math.cos(y * scale) * 10 + 
             Math.sin(x * scale * 2) * Math.cos(y * scale * 2) * 5 +
             Math.sin(x * scale * 4) * Math.cos(y * scale * 4) * 2;
    };
    
    // Apply noise to vertices
    for (let i = 0; i < vertices.length; i += 3) {
      const x = vertices[i];
      const y = vertices[i + 1];
      vertices[i + 2] = noise(x, y);
    }
    
    geometry.attributes.position.needsUpdate = true;
    geometry.computeVertexNormals();
    
    return geometry;
  }, [width, height, resolution]);
  
  // Set terrain data in store
  useEffect(() => {
    setTerrainData(terrainGeometry);
  }, [terrainGeometry, setTerrainData]);
  
  // Create snow material
  const snowMaterial = useMemo(() => {
    return new THREE.MeshLambertMaterial({
      color: 0xffffff,
      transparent: false,
      side: THREE.DoubleSide
    });
  }, []);
  
  // Add trees and rocks as decorations
  const decorations = useMemo(() => {
    const decorationsArray = [];
    const numTrees = 30;
    const numRocks = 20;
    
    // Generate trees
    for (let i = 0; i < numTrees; i++) {
      const x = (Math.random() - 0.5) * width * 0.8;
      const z = (Math.random() - 0.5) * height * 0.8;
      const y = noise(x, z) + 0.5; // Place on terrain surface
      
      decorationsArray.push({
        type: 'tree',
        position: [x, y, z],
        scale: 0.8 + Math.random() * 0.4
      });
    }
    
    // Generate rocks
    for (let i = 0; i < numRocks; i++) {
      const x = (Math.random() - 0.5) * width * 0.8;
      const z = (Math.random() - 0.5) * height * 0.8;
      const y = noise(x, z) + 0.2;
      
      decorationsArray.push({
        type: 'rock',
        position: [x, y, z],
        scale: 0.3 + Math.random() * 0.5
      });
    }
    
    return decorationsArray;
  }, [width, height]);
  
  // Simple noise function for decorations
  const noise = (x: number, y: number): number => {
    const scale = 0.02;
    return Math.sin(x * scale) * Math.cos(y * scale) * 10 + 
           Math.sin(x * scale * 2) * Math.cos(y * scale * 2) * 5 +
           Math.sin(x * scale * 4) * Math.cos(y * scale * 4) * 2;
  };
  
  return (
    <group>
      {/* Main terrain mesh */}
      <mesh
        ref={meshRef}
        geometry={terrainGeometry}
        material={snowMaterial}
        rotation={[-Math.PI / 2, 0, 0]}
        receiveShadow
        castShadow
      />
      
      {/* Tree decorations */}
      {decorations.filter(d => d.type === 'tree').map((tree, index) => (
        <group key={`tree-${index}`} position={tree.position as [number, number, number]}>
          {/* Tree trunk */}
          <mesh position={[0, 1, 0]} castShadow>
            <cylinderGeometry args={[0.2, 0.3, 2, 8]} />
            <meshLambertMaterial color={0x8B4513} />
          </mesh>
          {/* Tree foliage */}
          <mesh position={[0, 2.5, 0]} castShadow>
            <coneGeometry args={[1.5, 3, 8]} />
            <meshLambertMaterial color={0x228B22} />
          </mesh>
        </group>
      ))}
      
      {/* Rock decorations */}
      {decorations.filter(d => d.type === 'rock').map((rock, index) => (
        <mesh 
          key={`rock-${index}`} 
          position={rock.position as [number, number, number]}
          scale={rock.scale}
          castShadow
        >
          <dodecahedronGeometry args={[1, 0]} />
          <meshLambertMaterial color={0x696969} />
        </mesh>
      ))}
    </group>
  );
};

export default Terrain;