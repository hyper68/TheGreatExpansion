import React, { useState, useEffect, useRef } from 'react';
import { useGameStore } from '../stores/gameStore';
import Camera2DController from './Camera2DController';

const Game2D: React.FC = () => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [isDrawing, setIsDrawing] = useState(false);
  const [currentPath, setCurrentPath] = useState<{x: number, y: number}[]>([]);
  const [canvasSize, setCanvasSize] = useState({ width: 800, height: 600 });
  
  const {
    buildings,
    pistes,
    lifts,
    activeSkiers,
    isBuildMode,
    selectedBuildingType,
    placeBuilding,
    createPiste,
    cameraPosition
  } = useGameStore();

  // Update canvas size
  useEffect(() => {
    const updateSize = () => {
      if (canvasRef.current) {
        const rect = canvasRef.current.getBoundingClientRect();
        setCanvasSize({ width: rect.width, height: rect.height });
      }
    };

    updateSize();
    window.addEventListener('resize', updateSize);
    return () => window.removeEventListener('resize', updateSize);
  }, []);

  // Canvas drawing
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    // Set canvas size
    canvas.width = canvasSize.width;
    canvas.height = canvasSize.height;

    // Clear canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw background with gradient for sky/snow effect
    const gradient = ctx.createLinearGradient(0, 0, 0, canvas.height);
    gradient.addColorStop(0, '#87CEEB'); // Sky blue
    gradient.addColorStop(0.3, '#ffffff'); // Snow white
    gradient.addColorStop(1, '#f0f8ff'); // Light snow
    ctx.fillStyle = gradient;
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Draw mountain silhouette in background
    ctx.fillStyle = 'rgba(100, 100, 120, 0.3)';
    ctx.beginPath();
    ctx.moveTo(0, canvas.height * 0.7);
    for (let x = 0; x <= canvas.width; x += 50) {
      const height = Math.sin(x * 0.01) * 50 + Math.cos(x * 0.005) * 30 + canvas.height * 0.3;
      ctx.lineTo(x, height);
    }
    ctx.lineTo(canvas.width, canvas.height);
    ctx.lineTo(0, canvas.height);
    ctx.closePath();
    ctx.fill();

    // Draw terrain elevation with contour lines
    ctx.strokeStyle = '#d0d0d0';
    ctx.lineWidth = 1;
    ctx.setLineDash([2, 2]);
    for (let elevation = 0; elevation < 100; elevation += 10) {
      const y = canvas.height - (elevation * 2) - 100;
      if (y > 0 && y < canvas.height) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(canvas.width, y);
        ctx.stroke();
      }
    }
    ctx.setLineDash([]);

    // Draw pistes (ski runs) with better visuals
    pistes.forEach(piste => {
      if (piste.nodes.length < 2) return;
      
      // Draw the main piste line
      ctx.strokeStyle = piste.difficulty === 'green' ? '#32CD32' : 
                      piste.difficulty === 'blue' ? '#4169E1' : '#2F2F2F';
      ctx.lineWidth = 8;
      ctx.lineCap = 'round';
      ctx.lineJoin = 'round';
      ctx.beginPath();
      
      piste.nodes.forEach((node, index) => {
        const x = (node.position.x + cameraPosition.x) * 2 + canvas.width / 2;
        const y = canvas.height - (node.position.y * 2) - (node.position.z + cameraPosition.z) * 2 - canvas.height / 4;
        
        if (index === 0) {
          ctx.moveTo(x, y);
        } else {
          ctx.lineTo(x, y);
        }
      });
      
      ctx.stroke();
      
      // Draw piste border for better visibility
      ctx.strokeStyle = 'rgba(255, 255, 255, 0.5)';
      ctx.lineWidth = 12;
      ctx.globalAlpha = 0.5;
      ctx.stroke();
      ctx.globalAlpha = 1;
      
      // Draw piste nodes
      piste.nodes.forEach(node => {
        const x = (node.position.x + cameraPosition.x) * 2 + canvas.width / 2;
        const y = canvas.height - (node.position.y * 2) - (node.position.z + cameraPosition.z) * 2 - canvas.height / 4;
        
        // Node shadow
        ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
        ctx.beginPath();
        ctx.arc(x + 2, y + 2, 8, 0, 2 * Math.PI);
        ctx.fill();
        
        // Node
        ctx.fillStyle = '#ffffff';
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.arc(x, y, 6, 0, 2 * Math.PI);
        ctx.fill();
        ctx.stroke();
      });
    });

    // Draw lifts with better visuals
    lifts.forEach(lift => {
      // Draw lift line
      const startX = (lift.startPosition.x + cameraPosition.x) * 2 + canvas.width / 2;
      const startY = canvas.height - (lift.startPosition.y * 2) - (lift.startPosition.z + cameraPosition.z) * 2 - canvas.height / 4;
      const endX = (lift.endPosition.x + cameraPosition.x) * 2 + canvas.width / 2;
      const endY = canvas.height - (lift.endPosition.y * 2) - (lift.endPosition.z + cameraPosition.z) * 2 - canvas.height / 4;
      
      // Lift cable
      ctx.strokeStyle = '#666666';
      ctx.lineWidth = 3;
      ctx.beginPath();
      ctx.moveTo(startX, startY);
      ctx.lineTo(endX, endY);
      ctx.stroke();
      
      // Draw lift chairs with better visuals
      lift.chairs.forEach((chair: any) => {
        const chairX = (chair.position.x + cameraPosition.x) * 2 + canvas.width / 2;
        const chairY = canvas.height - (chair.position.y * 2) - (chair.position.z + cameraPosition.z) * 2 - canvas.height / 4;
        
        // Chair shadow
        ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
        ctx.fillRect(chairX - 6, chairY + 8, 12, 4);
        
        // Chair
        ctx.fillStyle = chair.occupied ? '#ff4757' : '#3742fa';
        ctx.strokeStyle = '#2f3542';
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.rect(chairX - 6, chairY - 6, 12, 8);
        ctx.fill();
        ctx.stroke();
        
        // Chair support
        ctx.strokeStyle = '#666666';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(chairX, chairY - 6);
        ctx.lineTo(chairX, chairY - 15);
        ctx.stroke();
      });
      
      // Draw lift stations with better visuals
      [lift.startPosition, lift.endPosition].forEach((pos, index) => {
        const x = (pos.x + cameraPosition.x) * 2 + canvas.width / 2;
        const y = canvas.height - (pos.y * 2) - (pos.z + cameraPosition.z) * 2 - canvas.height / 4;
        
        // Station shadow
        ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
        ctx.fillRect(x - 18, y - 8, 36, 26);
        
        // Station base
        ctx.fillStyle = '#8B4513';
        ctx.strokeStyle = '#654321';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.rect(x - 16, y - 10, 32, 22);
        ctx.fill();
        ctx.stroke();
        
        // Station roof
        ctx.fillStyle = '#D2691E';
        ctx.beginPath();
        ctx.moveTo(x - 20, y - 10);
        ctx.lineTo(x, y - 20);
        ctx.lineTo(x + 20, y - 10);
        ctx.closePath();
        ctx.fill();
        ctx.stroke();
        
        // Station label
        ctx.fillStyle = '#ffffff';
        ctx.font = '10px Arial';
        ctx.textAlign = 'center';
        ctx.fillText(index === 0 ? 'Start' : 'End', x, y + 4);
      });
    });

    // Draw buildings with better visuals
    buildings.forEach(building => {
      const x = (building.position.x + cameraPosition.x) * 2 + canvas.width / 2;
      const y = canvas.height - (building.position.y * 2) - (building.position.z + cameraPosition.z) * 2 - canvas.height / 4;
      
      // Building shadow
      ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
      ctx.fillRect(x - 17, y - 8, 34, 26);
      
      // Building color based on type
      let buildingColor = '#cccccc';
      if (building.type.category === 'lift') buildingColor = '#8B4513';
      if (building.type.category === 'facility') buildingColor = '#4682B4';
      
      // Building base
      ctx.fillStyle = buildingColor;
      ctx.strokeStyle = '#2f3542';
      ctx.lineWidth = 2;
      ctx.beginPath();
      ctx.rect(x - 15, y - 10, 30, 20);
      ctx.fill();
      ctx.stroke();
      
      // Building roof
      ctx.fillStyle = '#a0a0a0';
      ctx.beginPath();
      ctx.moveTo(x - 18, y - 10);
      ctx.lineTo(x, y - 18);
      ctx.lineTo(x + 18, y - 10);
      ctx.closePath();
      ctx.fill();
      ctx.stroke();
      
      // Building type indicator
      ctx.fillStyle = '#ffffff';
      ctx.font = '10px Arial';
      ctx.textAlign = 'center';
      ctx.fillText(building.type.name.substring(0, 6), x, y + 4);
    });

    // Draw skiers with better visuals
    activeSkiers.forEach(skier => {
      const x = (skier.position.x + cameraPosition.x) * 2 + canvas.width / 2;
      const y = canvas.height - (skier.position.y * 2) - (skier.position.z + cameraPosition.z) * 2 - canvas.height / 4;
      
      // Skier color based on skill level
      const skierColor = skier.skillLevel === 'expert' ? '#2f3542' : 
                        skier.skillLevel === 'intermediate' ? '#3742fa' : '#32CD32';
      
      // Skier shadow
      ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
      ctx.beginPath();
      ctx.arc(x + 2, y + 8, 8, 0, 2 * Math.PI);
      ctx.fill();
      
      // Skier body
      ctx.fillStyle = skierColor;
      ctx.strokeStyle = '#2f3542';
      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.arc(x, y, 6, 0, 2 * Math.PI);
      ctx.fill();
      ctx.stroke();
      
      // Skis (angled for movement effect)
      ctx.strokeStyle = skierColor;
      ctx.lineWidth = 3;
      ctx.lineCap = 'round';
      ctx.beginPath();
      const skiAngle = Math.atan2(skier.targetPosition.z - skier.position.z, skier.targetPosition.x - skier.position.x);
      const skiLength = 12;
      ctx.moveTo(x - Math.cos(skiAngle) * skiLength/2, y + 4 - Math.sin(skiAngle) * skiLength/2);
      ctx.lineTo(x + Math.cos(skiAngle) * skiLength/2, y + 4 + Math.sin(skiAngle) * skiLength/2);
      ctx.stroke();
      
      // Ski poles
      ctx.strokeStyle = '#8B4513';
      ctx.lineWidth = 2;
      ctx.beginPath();
      ctx.moveTo(x - 4, y - 2);
      ctx.lineTo(x - 6, y - 8);
      ctx.moveTo(x + 4, y - 2);
      ctx.lineTo(x + 6, y - 8);
      ctx.stroke();
    });

    // Draw current drawing path
    if (isDrawing && currentPath.length > 0) {
      ctx.strokeStyle = '#ffff00';
      ctx.lineWidth = 3;
      ctx.setLineDash([5, 5]);
      ctx.beginPath();
      
      currentPath.forEach((point, index) => {
        if (index === 0) {
          ctx.moveTo(point.x, point.y);
        } else {
          ctx.lineTo(point.x, point.y);
        }
      });
      
      ctx.stroke();
      ctx.setLineDash([]);
      
      // Draw path nodes
      currentPath.forEach(point => {
        ctx.fillStyle = '#ffff00';
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.arc(point.x, point.y, 5, 0, 2 * Math.PI);
        ctx.fill();
        ctx.stroke();
      });
    }

  }, [buildings, pistes, lifts, activeSkiers, cameraPosition, isDrawing, currentPath, canvasSize]);

  // Handle canvas click for building placement
  const handleCanvasClick = (event: React.MouseEvent<HTMLCanvasElement>) => {
    if (!isBuildMode || !selectedBuildingType) return;
    
    const canvas = canvasRef.current;
    if (!canvas) return;
    
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left - canvas.width / 2) / 2 - cameraPosition.x;
    const y = 0; // Ground level
    const z = (canvas.height - (event.clientY - rect.top) - canvas.height / 4) / 2 - cameraPosition.z;
    
    placeBuilding({ x, y, z } as any);
  };

  // Handle piste drawing
  const handlePisteMouseDown = (event: React.MouseEvent<HTMLCanvasElement>) => {
    if (!isBuildMode || selectedBuildingType) return;
    
    const canvas = canvasRef.current;
    if (!canvas) return;
    
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    setIsDrawing(true);
    setCurrentPath([{ x, y }]);
  };

  const handlePisteMouseMove = (event: React.MouseEvent<HTMLCanvasElement>) => {
    if (!isDrawing) return;
    
    const canvas = canvasRef.current;
    if (!canvas) return;
    
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    setCurrentPath(prev => [...prev, { x, y }]);
  };

  const handlePisteMouseUp = () => {
    if (!isDrawing || currentPath.length < 2) {
      setIsDrawing(false);
      setCurrentPath([]);
      return;
    }
    
    const canvas = canvasRef.current;
    if (!canvas) return;
    
    // Convert canvas coordinates to world coordinates
    const worldPath = currentPath.map(point => ({
      x: (point.x - canvas.width / 2) / 2 - cameraPosition.x,
      y: 0,
      z: (canvas.height - point.y - canvas.height / 4) / 2 - cameraPosition.z
    }));
    
    createPiste(worldPath as any);
    setIsDrawing(false);
    setCurrentPath([]);
  };

  return (
    <Camera2DController>
      <canvas
        ref={canvasRef}
        className="w-full h-full"
        onClick={handleCanvasClick}
        onMouseDown={handlePisteMouseDown}
        onMouseMove={handlePisteMouseMove}
        onMouseUp={handlePisteMouseUp}
      />
      
      {/* Instructions */}
      {isBuildMode && (
        <div className="absolute top-4 left-4 bg-black/70 text-white p-3 rounded-lg text-sm z-10">
          {selectedBuildingType ? (
            <div>
              <div>Click to place {selectedBuildingType.name}</div>
              <div className="text-xs opacity-75">Right-click to cancel</div>
            </div>
          ) : (
            <div>
              <div>Click and drag to draw ski runs</div>
              <div className="text-xs opacity-75">Right-click to finish drawing</div>
            </div>
          )}
        </div>
      )}
    </Camera2DController>
  );
};

export default Game2D;