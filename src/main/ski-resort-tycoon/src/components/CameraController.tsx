import React, { useRef, useEffect } from 'react';
import { useFrame, useThree } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import * as THREE from 'three';
import { useGameStore } from '../stores/gameStore';

const CameraController: React.FC = () => {
  const controlsRef = useRef<any>(null);
  const { camera, gl } = useThree();
  const { cameraPosition, cameraTarget, updateCamera, isBuildMode } = useGameStore();
  
  // Set up isometric camera configuration
  useEffect(() => {
    if (controlsRef.current) {
      // Lock rotation for isometric view (45 degrees around Y axis)
      controlsRef.current.minPolarAngle = Math.PI / 6; // 30 degrees from horizontal
      controlsRef.current.maxPolarAngle = Math.PI / 3; // 60 degrees from horizontal
      
      // Set initial rotation for isometric view
      controlsRef.current.setAzimuthalAngle(-Math.PI / 4); // 45 degrees
      
      // Enable panning and zooming
      controlsRef.current.enablePan = true;
      controlsRef.current.enableZoom = true;
      controlsRef.current.enableRotate = !isBuildMode; // Disable rotation in build mode
      
      // Set zoom limits
      controlsRef.current.minDistance = 20;
      controlsRef.current.maxDistance = 200;
      
      // Set pan limits
      controlsRef.current.minAzimuthAngle = -Math.PI / 4 - 0.1;
      controlsRef.current.maxAzimuthAngle = -Math.PI / 4 + 0.1;
    }
  }, [isBuildMode]);
  
  // Handle keyboard controls (WASD + arrow keys)
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (!controlsRef.current) return;
      
      const moveSpeed = 2;
      const currentTarget = controlsRef.current.target.clone();
      
      switch (event.key.toLowerCase()) {
        case 'w':
        case 'arrowup':
          currentTarget.z -= moveSpeed;
          break;
        case 's':
        case 'arrowdown':
          currentTarget.z += moveSpeed;
          break;
        case 'a':
        case 'arrowleft':
          currentTarget.x -= moveSpeed;
          break;
        case 'd':
        case 'arrowright':
          currentTarget.x += moveSpeed;
          break;
      }
      
      controlsRef.current.target.copy(currentTarget);
      updateCamera(camera.position, currentTarget);
    };
    
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [updateCamera]);
  
  // Handle mouse wheel zoom
  useEffect(() => {
    const handleWheel = (event: WheelEvent) => {
      if (!controlsRef.current) return;
      
      event.preventDefault();
      const zoomSpeed = 0.1;
      const currentDistance = controlsRef.current.getDistance();
      
      if (event.deltaY < 0) {
        // Zoom in
        controlsRef.current.setDistance(Math.max(20, currentDistance * (1 - zoomSpeed)));
      } else {
        // Zoom out
        controlsRef.current.setDistance(Math.min(200, currentDistance * (1 + zoomSpeed)));
      }
    };
    
    gl.domElement.addEventListener('wheel', handleWheel, { passive: false });
    return () => gl.domElement.removeEventListener('wheel', handleWheel);
  }, [gl]);
  
  // Update camera position in store when controls change
  useFrame(() => {
    if (controlsRef.current) {
      updateCamera(controlsRef.current.object.position, controlsRef.current.target);
    }
  });
  
  return (
    <OrbitControls
      ref={controlsRef}
      args={[camera, gl.domElement]}
      enableDamping
      dampingFactor={0.05}
      screenSpacePanning={false}
      makeDefault
    />
  );
};

export default CameraController;