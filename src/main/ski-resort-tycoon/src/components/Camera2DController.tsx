import React, { useRef, useEffect, useState } from 'react';
import { useGameStore } from '../stores/gameStore';

interface Camera2DControllerProps {
  children: React.ReactNode;
}

const Camera2DController: React.FC<Camera2DControllerProps> = ({ children }) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const [isDragging, setIsDragging] = useState(false);
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 });
  const [lastPanDelta, setLastPanDelta] = useState({ x: 0, y: 0 });
  
  const { cameraPosition, updateCamera, isBuildMode } = useGameStore();
  const [zoom, setZoom] = useState(1);
  const [panOffset, setPanOffset] = useState({ x: 0, y: 0 });

  // Handle mouse drag for panning
  const handleMouseDown = (event: React.MouseEvent) => {
    if (isBuildMode) return; // Don't pan in build mode
    
    setIsDragging(true);
    setDragStart({ x: event.clientX, y: event.clientY });
    setLastPanDelta({ x: 0, y: 0 });
  };

  const handleMouseMove = (event: React.MouseEvent) => {
    if (!isDragging || isBuildMode) return;
    
    const deltaX = event.clientX - dragStart.x;
    const deltaY = event.clientY - dragStart.y;
    
    // Update pan offset
    const newPanOffset = {
      x: panOffset.x + deltaX - lastPanDelta.x,
      y: panOffset.y + deltaY - lastPanDelta.y
    };
    
    setPanOffset(newPanOffset);
    setLastPanDelta({ x: deltaX, y: deltaY });
    
    // Update camera position in store
    const newCameraPos = {
      x: cameraPosition.x - deltaX * 0.5 / zoom,
      y: cameraPosition.y,
      z: cameraPosition.z - deltaY * 0.5 / zoom
    };
    
    updateCamera(newCameraPos as any, { x: 0, y: 0, z: 0 } as any);
  };

  const handleMouseUp = () => {
    setIsDragging(false);
    setLastPanDelta({ x: 0, y: 0 });
  };

  // Handle wheel zoom
  const handleWheel = (event: React.WheelEvent) => {
    event.preventDefault();
    
    const zoomFactor = event.deltaY > 0 ? 0.9 : 1.1;
    const newZoom = Math.max(0.1, Math.min(3, zoom * zoomFactor));
    setZoom(newZoom);
  };

  // Handle keyboard controls (WASD + arrow keys)
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      const moveSpeed = 5 / zoom;
      let newCameraPos = { ...cameraPosition };
      
      switch (event.key.toLowerCase()) {
        case 'w':
        case 'arrowup':
          newCameraPos.z -= moveSpeed;
          break;
        case 's':
        case 'arrowdown':
          newCameraPos.z += moveSpeed;
          break;
        case 'a':
        case 'arrowleft':
          newCameraPos.x -= moveSpeed;
          break;
        case 'd':
        case 'arrowright':
          newCameraPos.x += moveSpeed;
          break;
        case 'r':
          // Reset camera
          newCameraPos = { x: 0, y: 50, z: 0 };
          setZoom(1);
          setPanOffset({ x: 0, y: 0 });
          break;
      }
      
      updateCamera(newCameraPos as any, { x: 0, y: 0, z: 0 } as any);
    };
    
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [cameraPosition, zoom, updateCamera]);

  // Update cursor style
  const getCursorStyle = () => {
    if (isBuildMode) return 'crosshair';
    if (isDragging) return 'grabbing';
    return 'grab';
  };

  return (
    <div 
      ref={containerRef}
      className="w-full h-full overflow-hidden relative"
      onMouseDown={handleMouseDown}
      onMouseMove={handleMouseMove}
      onMouseUp={handleMouseUp}
      onMouseLeave={handleMouseUp}
      onWheel={handleWheel}
      style={{ cursor: getCursorStyle() }}
    >
      <div 
        className="w-full h-full"
        style={{
          transform: `translate(${panOffset.x}px, ${panOffset.y}px) scale(${zoom})`,
          transformOrigin: 'center center'
        }}
      >
        {children}
      </div>
      
      {/* Camera controls UI */}
      <div className="absolute bottom-4 right-4 bg-black/70 text-white p-3 rounded-lg text-sm">
        <div className="mb-2 font-semibold">Camera Controls</div>
        <div className="text-xs space-y-1">
          <div>WASD / Arrow Keys: Pan camera</div>
          <div>Mouse Wheel: Zoom in/out</div>
          <div>Mouse Drag: Pan camera</div>
          <div>R: Reset camera</div>
        </div>
        <div className="mt-2 text-xs">
          Zoom: {Math.round(zoom * 100)}%
        </div>
      </div>
    </div>
  );
};

export default Camera2DController;