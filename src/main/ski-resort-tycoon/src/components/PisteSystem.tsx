import React, { useRef, useState, useEffect } from 'react';
import { useFrame, useThree } from '@react-three/fiber';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';

const PisteSystem: React.FC = () => {
  const { pistes, isBuildMode, createPiste } = useGameStore();
  const { camera, raycaster, mouse, scene } = useThree();
  const [isDrawing, setIsDrawing] = useState(false);
  const [currentNodes, setCurrentNodes] = useState<THREE.Vector3[]>([]);
  const [previewLine, setPreviewLine] = useState<THREE.Vector3 | null>(null);
  
  // Handle piste drawing in build mode
  useFrame(() => {
    if (!isBuildMode || !isDrawing) return;
    
    // Raycast to terrain for node placement
    raycaster.setFromCamera(mouse, camera);
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
      position.y += 0.1; // Slightly above terrain
      setPreviewLine(position);
    }
  });
  
  // Handle mouse events for piste creation
  useEffect(() => {
    if (!isBuildMode) return;
    
    const handleMouseDown = (event: MouseEvent) => {
      if (event.button === 0) { // Left click
        if (!isDrawing) {
          setIsDrawing(true);
          setCurrentNodes([]);
        }
        
        // Add node at current mouse position
        if (previewLine) {
          setCurrentNodes(prev => [...prev, previewLine.clone()]);
        }
      } else if (event.button === 2) { // Right click - finish drawing
        if (isDrawing && currentNodes.length >= 2) {
          createPiste(currentNodes);
        }
        setIsDrawing(false);
        setCurrentNodes([]);
        setPreviewLine(null);
      }
    };
    
    const handleContextMenu = (event: Event) => {
      event.preventDefault();
    };
    
    window.addEventListener('mousedown', handleMouseDown);
    window.addEventListener('contextmenu', handleContextMenu);
    
    return () => {
      window.removeEventListener('mousedown', handleMouseDown);
      window.removeEventListener('contextmenu', handleContextMenu);
    };
  }, [isBuildMode, isDrawing, previewLine, currentNodes, createPiste]);
  
  // Render existing pistes
  const renderPistes = () => {
    return pistes.map((piste) => (
      <PistePath key={piste.id} piste={piste} />
    ));
  };
  
  // Render current drawing preview
  const renderDrawingPreview = () => {
    if (!isDrawing) return null;
    
    const allNodes = [...currentNodes];
    if (previewLine) {
      allNodes.push(previewLine);
    }
    
    return (
      <PistePreview 
        nodes={allNodes} 
        isActive={isDrawing}
      />
    );
  };
  
  return (
    <>
      {renderPistes()}
      {renderDrawingPreview()}
    </>
  );
};

// Piste Path Component
interface PistePathProps {
  piste: any;
}

const PistePath: React.FC<PistePathProps> = ({ piste }) => {
  const meshRef = useRef<THREE.Group>(null);
  
  // Get color based on difficulty
  const getDifficultyColor = (difficulty: string): number => {
    switch (difficulty) {
      case 'green': return 0x00ff00;
      case 'blue': return 0x0000ff;
      case 'black': return 0x000000;
      default: return 0xffffff;
    }
  };
  
  // Create path geometry
  const createPathGeometry = (): THREE.BufferGeometry => {
    if (piste.nodes.length < 2) return new THREE.BufferGeometry();
    
    const points = piste.nodes.map((node: any) => node.position);
    const curve = new THREE.CatmullRomCurve3(points);
    const tubeGeometry = new THREE.TubeGeometry(curve, 64, 0.5, 8, false);
    
    return tubeGeometry;
  };
  
  // Create node markers
  const renderNodeMarkers = () => {
    return piste.nodes.map((node: any, index: number) => (
      <mesh key={`node-${piste.id}-${index}`} position={node.position.toArray()}>
        <sphereGeometry args={[0.3, 8, 8]} />
        <meshLambertMaterial color={getDifficultyColor(piste.difficulty)} />
      </mesh>
    ));
  };
  
  return (
    <group ref={meshRef}>
      {/* Piste path */}
      <mesh geometry={createPathGeometry()} castShadow receiveShadow>
        <meshLambertMaterial 
          color={getDifficultyColor(piste.difficulty)} 
          transparent 
          opacity={0.8}
        />
      </mesh>
      
      {/* Node markers */}
      {renderNodeMarkers()}
      
      {/* Piste label */}
      <mesh position={[
        piste.nodes[0].position.x,
        piste.nodes[0].position.y + 2,
        piste.nodes[0].position.z
      ]}>
        <boxGeometry args={[2, 0.5, 0.1]} />
        <meshLambertMaterial color={0xffffff} />
      </mesh>
    </group>
  );
};

// Piste Preview Component
interface PistePreviewProps {
  nodes: THREE.Vector3[];
  isActive: boolean;
}

const PistePreview: React.FC<PistePreviewProps> = ({ nodes, isActive }) => {
  if (nodes.length < 2) return null;
  
  const points = nodes;
  const curve = new THREE.CatmullRomCurve3(points);
  const tubeGeometry = new THREE.TubeGeometry(curve, 32, 0.3, 8, false);
  
  return (
    <group>
      {/* Preview path */}
      <mesh geometry={tubeGeometry}>
        <meshBasicMaterial 
          color={0xffff00} 
          transparent 
          opacity={isActive ? 0.6 : 0.3}
        />
      </mesh>
      
      {/* Preview nodes */}
      {nodes.map((node, index) => (
        <mesh key={`preview-node-${index}`} position={node.toArray()}>
          <sphereGeometry args={[0.2, 8, 8]} />
          <meshBasicMaterial color={0xffff00} transparent opacity={0.7} />
        </mesh>
      ))}
    </group>
  );
};

export default PisteSystem;