import React, { useRef, useState, useEffect } from 'react';
import { useFrame, useThree } from '@react-three/fiber';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';
import { BUILDING_TYPES } from '../config/buildings';

const BuildingSystem: React.FC = () => {
  const { buildings, selectedBuildingType, isBuildMode, placeBuilding } = useGameStore();
  const { camera, raycaster, mouse, scene } = useThree();
  const [previewPosition, setPreviewPosition] = useState<THREE.Vector3 | null>(null);
  const [isValidPlacement, setIsValidPlacement] = useState(true);
  const previewRef = useRef<THREE.Group>(null);
  
  // Raycast to terrain for building placement
  useFrame(() => {
    if (!isBuildMode || !selectedBuildingType) return;
    
    // Create a ray from camera to mouse position
    raycaster.setFromCamera(mouse, camera);
    
    // Find intersection with terrain
    const terrainIntersects = raycaster.intersectObjects(
      scene.children.filter(child => 
        child.type === 'Mesh' && 
        child.geometry && 
        child.geometry.type === 'PlaneGeometry'
      )
    );
    
    if (terrainIntersects.length > 0) {
      const intersection = terrainIntersects[0];
      const position = intersection.point;
      
      // Snap to grid (2m x 2m cells)
      position.x = Math.round(position.x / 2) * 2;
      position.z = Math.round(position.z / 2) * 2;
      position.y = intersection.point.y + 0.1; // Slightly above terrain
      
      setPreviewPosition(position);
      
      // Validate placement
      const isValid = validatePlacement(position, selectedBuildingType);
      setIsValidPlacement(isValid);
    } else {
      setPreviewPosition(null);
    }
  });
  
  // Handle mouse clicks for placement
  useEffect(() => {
    if (!isBuildMode || !selectedBuildingType) return;
    
    const handleClick = (event: MouseEvent) => {
      if (event.button === 0 && previewPosition && isValidPlacement) { // Left click
        placeBuilding(previewPosition);
      } else if (event.button === 2) { // Right click - cancel
        // Could implement cancel logic here
      }
    };
    
    const handleContextMenu = (event: Event) => {
      event.preventDefault(); // Prevent context menu
    };
    
    window.addEventListener('mousedown', handleClick);
    window.addEventListener('contextmenu', handleContextMenu);
    
    return () => {
      window.removeEventListener('mousedown', handleClick);
      window.removeEventListener('contextmenu', handleContextMenu);
    };
  }, [isBuildMode, selectedBuildingType, previewPosition, isValidPlacement, placeBuilding]);
  
  // Validate building placement
  const validatePlacement = (position: THREE.Vector3, buildingType: any): boolean => {
    // Check if position is within bounds
    if (Math.abs(position.x) > 100 || Math.abs(position.z) > 100) return false;
    
    // Check for collisions with existing buildings
    const minDistance = 5; // Minimum distance between buildings
    for (const building of buildings) {
      const distance = position.distanceTo(building.position);
      if (distance < minDistance) return false;
    }
    
    // Check terrain slope (buildings need relatively flat ground)
    const slopeAngle = calculateSlopeAngle(position);
    if (slopeAngle > 10) return false; // Max 10 degree slope for buildings
    
    return true;
  };
  
  // Calculate slope angle at position
  const calculateSlopeAngle = (position: THREE.Vector3): number => {
    // This is a simplified calculation
    // In a real implementation, you'd sample the terrain height at multiple points
    const heightVariation = Math.abs(Math.sin(position.x * 0.1) * Math.cos(position.z * 0.1)) * 5;
    return Math.atan2(heightVariation, 2) * 180 / Math.PI;
  };
  
  // Render existing buildings
  const renderBuildings = () => {
    return buildings.map((building) => (
      <BuildingModel
        key={building.id}
        building={building}
        position={building.position}
        rotation={[0, building.rotation, 0]}
      />
    ));
  };
  
  // Render placement preview
  const renderPreview = () => {
    if (!isBuildMode || !selectedBuildingType || !previewPosition) return null;
    
    return (
      <group ref={previewRef} position={previewPosition.toArray()}>
        <BuildingModel
          building={{
            id: 'preview',
            type: selectedBuildingType,
            position: previewPosition,
            rotation: 0,
            builtAt: new Date()
          }}
          isPreview={true}
          isValid={isValidPlacement}
        />
        
        {/* Placement indicator */}
        <mesh position={[0, 0.01, 0]} rotation={[-Math.PI / 2, 0, 0]}>
          <ringGeometry args={[1, 1.5, 16]} />
          <meshBasicMaterial 
            color={isValidPlacement ? 0x00ff00 : 0xff0000} 
            transparent 
            opacity={0.7}
          />
        </mesh>
      </group>
    );
  };
  
  return (
    <>
      {renderBuildings()}
      {renderPreview()}
    </>
  );
};

// Building Model Component
interface BuildingModelProps {
  building: any;
  position: [number, number, number];
  rotation?: [number, number, number];
  isPreview?: boolean;
  isValid?: boolean;
}

const BuildingModel: React.FC<BuildingModelProps> = ({ 
  building, 
  position, 
  rotation = [0, 0, 0],
  isPreview = false,
  isValid = true
}) => {
  const meshRef = useRef<THREE.Group>(null);
  
  // Simple placeholder models based on building type
  const renderModel = () => {
    const opacity = isPreview ? 0.7 : 1;
    const color = isPreview && !isValid ? 0xff4444 : 0xcccccc;
    
    switch (building.type.category) {
      case 'lift':
        return (
          <group>
            {/* Lift tower */}
            <mesh position={[0, 2, 0]} castShadow>
              <cylinderGeometry args={[0.2, 0.2, 4, 8]} />
              <meshLambertMaterial color={0x666666} transparent={isPreview} opacity={opacity} />
            </mesh>
            {/* Lift base */}
            <mesh position={[0, 0.1, 0]} castShadow>
              <cylinderGeometry args={[1, 1, 0.2, 8]} />
              <meshLambertMaterial color={0x888888} transparent={isPreview} opacity={opacity} />
            </mesh>
          </group>
        );
        
      case 'building':
        return (
          <group>
            {/* Building base */}
            <mesh position={[0, 1, 0]} castShadow>
              <boxGeometry args={[3, 2, 2]} />
              <meshLambertMaterial color={color} transparent={isPreview} opacity={opacity} />
            </mesh>
            {/* Roof */}
            <mesh position={[0, 2.2, 0]} castShadow>
              <coneGeometry args={[2.2, 0.4, 4]} />
              <meshLambertMaterial color={0x8B4513} transparent={isPreview} opacity={opacity} />
            </mesh>
          </group>
        );
        
      case 'facility':
        return (
          <group>
            {/* Facility base */}
            <mesh position={[0, 0.5, 0]} castShadow>
              <boxGeometry args={[2, 1, 2]} />
              <meshLambertMaterial color={color} transparent={isPreview} opacity={opacity} />
            </mesh>
            {/* Facility details */}
            <mesh position={[0, 1.2, 0]} castShadow>
              <boxGeometry args={[0.5, 0.4, 0.5]} />
              <meshLambertMaterial color={0x444444} transparent={isPreview} opacity={opacity} />
            </mesh>
          </group>
        );
        
      default:
        return (
          <mesh castShadow>
            <boxGeometry args={[1, 1, 1]} />
            <meshLambertMaterial color={color} transparent={isPreview} opacity={opacity} />
          </mesh>
        );
    }
  };
  
  return (
    <group ref={meshRef} position={position} rotation={rotation} castShadow>
      {renderModel()}
    </group>
  );
};

export default BuildingSystem;