import React, { useRef, useMemo } from 'react';
import { useFrame } from '@react-three/fiber';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';

const LiftSystem: React.FC = () => {
  const { lifts } = useGameStore();
  
  return (
    <>
      {lifts.map((lift) => (
        <LiftComponent key={lift.id} lift={lift} />
      ))}
    </>
  );
};

// Individual Lift Component
interface LiftComponentProps {
  lift: any;
}

const LiftComponent: React.FC<LiftComponentProps> = ({ lift }) => {
  const groupRef = useRef<THREE.Group>(null);
  
  // Animate lift chairs
  useFrame((state, delta) => {
    if (!groupRef.current) return;
    
    // Update chair positions along the lift line
    lift.chairs.forEach((chair: any, index: number) => {
      const chairMesh = groupRef.current?.children[index] as THREE.Mesh;
      if (chairMesh) {
        // Move chair along the lift line
        chair.progress = (chair.progress + delta * 0.1) % 1;
        
        // Interpolate position between start and end
        const newPosition = lift.startPosition.clone().lerp(lift.endPosition, chair.progress);
        chairMesh.position.copy(newPosition);
        
        // Update chair data
        chair.position = newPosition;
      }
    });
  });
  
  // Create lift cable
  const cableGeometry = useMemo(() => {
    const points = [lift.startPosition, lift.endPosition];
    const curve = new THREE.LineCurve3(points[0], points[1]);
    return new THREE.TubeGeometry(curve, 32, 0.1, 8, false);
  }, [lift.startPosition, lift.endPosition]);
  
  // Create support towers
  const createSupportTowers = () => {
    const distance = lift.startPosition.distanceTo(lift.endPosition);
    const numTowers = Math.floor(distance / 30) + 1;
    const towers = [];
    
    for (let i = 1; i < numTowers; i++) {
      const progress = i / numTowers;
      const position = lift.startPosition.clone().lerp(lift.endPosition, progress);
      position.y += 5; // Tower height
      
      towers.push(
        <mesh key={`tower-${i}`} position={position.toArray()} castShadow>
          <cylinderGeometry args={[0.3, 0.3, 10, 8]} />
          <meshLambertMaterial color={0x666666} />
        </mesh>
      );
    }
    
    return towers;
  };
  
  return (
    <group ref={groupRef}>
      {/* Lift cable */}
      <mesh geometry={cableGeometry} castShadow>
        <meshLambertMaterial color={0x444444} />
      </mesh>
      
      {/* Support towers */}
      {createSupportTowers()}
      
      {/* Lift stations */}
      <LiftStation position={lift.startPosition} isStart={true} />
      <LiftStation position={lift.endPosition} isStart={false} />
      
      {/* Lift chairs */}
      {lift.chairs.map((chair: any, index: number) => (
        <LiftChair key={chair.id} chair={chair} />
      ))}
    </group>
  );
};

// Lift Station Component
interface LiftStationProps {
  position: THREE.Vector3;
  isStart: boolean;
}

const LiftStation: React.FC<LiftStationProps> = ({ position, isStart }) => {
  return (
    <group position={position.toArray()}>
      {/* Station building */}
      <mesh position={[0, 1, 0]} castShadow>
        <boxGeometry args={[4, 2, 3]} />
        <meshLambertMaterial color={0x8B4513} />
      </mesh>
      
      {/* Station roof */}
      <mesh position={[0, 2.2, 0]} castShadow>
        <coneGeometry args={[2.5, 0.4, 4]} />
        <meshLambertMaterial color={0x654321} />
      </mesh>
      
      {/* Queue area */}
      <mesh position={[isStart ? -3 : 3, 0.1, 0]} rotation={[-Math.PI / 2, 0, 0]}>
        <planeGeometry args={[3, 2]} />
        <meshLambertMaterial color={0xcccccc} transparent opacity={0.7} />
      </mesh>
      
      {/* Station sign */}
      <mesh position={[0, 3, 0]} castShadow>
        <boxGeometry args={[2, 0.5, 0.1]} />
        <meshLambertMaterial color={0xffffff} />
      </mesh>
    </group>
  );
};

// Lift Chair Component
interface LiftChairProps {
  chair: any;
}

const LiftChair: React.FC<LiftChairProps> = ({ chair }) => {
  return (
    <group position={chair.position.toArray()}>
      {/* Chair seat */}
      <mesh position={[0, 0.5, 0]} castShadow>
        <boxGeometry args={[1, 0.2, 1]} />
        <meshLambertMaterial color={chair.occupied ? 0xff6b6b : 0x4ecdc4} />
      </mesh>
      
      {/* Chair back */}
      <mesh position={[0, 1, -0.4]} castShadow>
        <boxGeometry args={[1, 1, 0.2]} />
        <meshLambertMaterial color={chair.occupied ? 0xff6b6b : 0x4ecdc4} />
      </mesh>
      
      {/* Chair support */}
      <mesh position={[0, 2, 0]} castShadow>
        <cylinderGeometry args={[0.05, 0.05, 1.5, 8]} />
        <meshLambertMaterial color={0x333333} />
      </mesh>
      
      {/* Cable attachment */}
      <mesh position={[0, 2.8, 0]} castShadow>
        <sphereGeometry args={[0.1, 8, 8]} />
        <meshLambertMaterial color={0x444444} />
      </mesh>
    </group>
  );
};

export default LiftSystem;