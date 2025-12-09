import React, { useRef, useMemo } from 'react';
import { useFrame } from '@react-three/fiber';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';

const SkierNPCSystem: React.FC = () => {
  const { activeSkiers, updateSkierPositions, pistes, lifts } = useGameStore();
  
  // Update skier positions
  useFrame((state, delta) => {
    updateSkierPositions(delta);
  });
  
  return (
    <>
      {activeSkiers.map((skier) => (
        <SkierComponent key={skier.id} skier={skier} />
      ))}
    </>
  );
};

// Individual Skier Component
interface SkierComponentProps {
  skier: any;
}

const SkierComponent: React.FC<SkierComponentProps> = ({ skier }) => {
  const meshRef = useRef<THREE.Group>(null);
  
  // Simple AI behavior
  useFrame((state, delta) => {
    if (!meshRef.current) return;
    
    // Update skier orientation based on movement direction
    // This is simplified - in a real implementation you'd have proper pathfinding
    const direction = skier.targetPosition.clone().sub(skier.position).normalize();
    if (direction.length() > 0.1) {
      meshRef.current.lookAt(skier.position.clone().add(direction));
    }
  });
  
  // Get skier appearance based on skill level
  const getSkierAppearance = () => {
    switch (skier.skillLevel) {
      case 'beginner':
        return { color: 0x00ff00, size: 0.8 }; // Green
      case 'intermediate':
        return { color: 0x0000ff, size: 1.0 }; // Blue
      case 'expert':
        return { color: 0x000000, size: 1.2 }; // Black
      default:
        return { color: 0x888888, size: 1.0 };
    }
  };
  
  const appearance = getSkierAppearance();
  
  return (
    <group ref={meshRef} position={skier.position.toArray()} castShadow>
      {/* Skier body */}
      <mesh position={[0, 0.8, 0]} castShadow>
        <capsuleGeometry args={[0.2 * appearance.size, 0.6 * appearance.size, 4, 8]} />
        <meshLambertMaterial color={appearance.color} />
      </mesh>
      
      {/* Skis */}
      <mesh position={[0, 0.1, 0]} rotation={[0, 0, Math.PI / 12]} castShadow>
        <boxGeometry args={[0.1, 0.05, 1.5 * appearance.size]} />
        <meshLambertMaterial color={0xffffff} />
      </mesh>
      <mesh position={[0, 0.1, 0]} rotation={[0, 0, -Math.PI / 12]} castShadow>
        <boxGeometry args={[0.1, 0.05, 1.5 * appearance.size]} />
        <meshLambertMaterial color={0xffffff} />
      </mesh>
      
      {/* Ski poles */}
      <mesh position={[-0.3, 0.5, 0]} rotation={[Math.PI / 6, 0, Math.PI / 4]} castShadow>
        <cylinderGeometry args={[0.02, 0.02, 0.8, 8]} />
        <meshLambertMaterial color={0x8B4513} />
      </mesh>
      <mesh position={[0.3, 0.5, 0]} rotation={[Math.PI / 6, 0, -Math.PI / 4]} castShadow>
        <cylinderGeometry args={[0.02, 0.02, 0.8, 8]} />
        <meshLambertMaterial color={0x8B4513} />
      </mesh>
      
      {/* Helmet */}
      <mesh position={[0, 1.1, 0]} castShadow>
        <sphereGeometry args={[0.25 * appearance.size, 8, 8]} />
        <meshLambertMaterial color={appearance.color} />
      </mesh>
    </group>
  );
};

// Pathfinding utility functions
export class SkierPathfinding {
  static findPath(start: THREE.Vector3, end: THREE.Vector3, pistes: any[]): THREE.Vector3[] {
    // Simplified pathfinding - in a real implementation you'd use A* or similar
    const path: THREE.Vector3[] = [];
    
    // Find nearest piste to start and end
    const startPiste = this.findNearestPiste(start, pistes);
    const endPiste = this.findNearestPiste(end, pistes);
    
    if (startPiste && endPiste) {
      // Create path along piste nodes
      path.push(start);
      
      // Add piste nodes
      if (startPiste === endPiste) {
        // Same piste, follow nodes
        const startIndex = this.findNearestNodeIndex(start, startPiste.nodes);
        const endIndex = this.findNearestNodeIndex(end, endPiste.nodes);
        
        if (startIndex <= endIndex) {
          for (let i = startIndex; i <= endIndex; i++) {
            path.push(startPiste.nodes[i].position.clone());
          }
        } else {
          for (let i = startIndex; i >= endIndex; i--) {
            path.push(startPiste.nodes[i].position.clone());
          }
        }
      } else {
        // Different pistes - find connection (simplified)
        path.push(...this.findConnection(startPiste, endPiste));
      }
      
      path.push(end);
    } else {
      // Direct path if no pistes available
      path.push(start);
      path.push(end);
    }
    
    return path;
  }
  
  static findNearestPiste(position: THREE.Vector3, pistes: any[]): any | null {
    let nearestPiste = null;
    let minDistance = Infinity;
    
    for (const piste of pistes) {
      for (const node of piste.nodes) {
        const distance = position.distanceTo(node.position);
        if (distance < minDistance) {
          minDistance = distance;
          nearestPiste = piste;
        }
      }
    }
    
    return nearestPiste;
  }
  
  static findNearestNodeIndex(position: THREE.Vector3, nodes: any[]): number {
    let nearestIndex = 0;
    let minDistance = Infinity;
    
    for (let i = 0; i < nodes.length; i++) {
      const distance = position.distanceTo(nodes[i].position);
      if (distance < minDistance) {
        minDistance = distance;
        nearestIndex = i;
      }
    }
    
    return nearestIndex;
  }
  
  static findConnection(startPiste: any, endPiste: any): THREE.Vector3[] {
    // Simplified connection finding - return direct path
    return [
      startPiste.nodes[startPiste.nodes.length - 1].position.clone(),
      endPiste.nodes[0].position.clone()
    ];
  }
}

export default SkierNPCSystem;