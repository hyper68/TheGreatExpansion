import { create } from 'zustand';
import * as THREE from 'three';

export interface BuildingType {
  id: string;
  name: string;
  cost: number;
  maintenanceCost: number;
  modelPath: string;
  category: 'lift' | 'building' | 'facility';
}

export interface PisteNode {
  id: string;
  position: THREE.Vector3;
  connections: string[];
}

export interface Piste {
  id: string;
  name: string;
  nodes: PisteNode[];
  difficulty: 'green' | 'blue' | 'black';
  length: number;
  slopeAngle: number;
}

export interface Lift {
  id: string;
  name: string;
  type: 'chairlift' | 'gondola';
  startPosition: THREE.Vector3;
  endPosition: THREE.Vector3;
  capacity: number;
  speed: number;
  ticketPrice: number;
  chairs: LiftChair[];
}

export interface LiftChair {
  id: string;
  position: THREE.Vector3;
  progress: number; // 0-1 along the lift line
  occupied: boolean;
}

export interface SkierNPC {
  id: string;
  position: THREE.Vector3;
  skillLevel: 'beginner' | 'intermediate' | 'expert';
  currentRun: Piste | null;
  satisfaction: number;
  moneySpent: number;
  targetPosition: THREE.Vector3;
  speed: number;
}

export interface EconomyState {
  currentMoney: number;
  dailyRevenue: number;
  dailyExpenses: number;
  guestSatisfaction: number;
  totalSkiers: number;
}

export interface GameState {
  // Game world
  currentResort: {
    id: string;
    name: string;
    money: number;
    reputation: number;
    guestCapacity: number;
  } | null;
  
  // 3D World state
  terrainData: THREE.BufferGeometry | null;
  buildings: Building[];
  pistes: Piste[];
  lifts: Lift[];
  activeSkiers: SkierNPC[];
  
  // UI State
  isBuildMode: boolean;
  selectedBuildingType: BuildingType | null;
  cameraPosition: THREE.Vector3;
  cameraTarget: THREE.Vector3;
  
  // Economy
  economy: EconomyState;
  
  // Game logic
  isPaused: boolean;
  gameSpeed: number; // 0 = paused, 1 = normal, 2 = fast
  
  // Actions
  placeBuilding: (position: THREE.Vector3) => void;
  createPiste: (nodes: THREE.Vector3[]) => void;
  createLift: (start: THREE.Vector3, end: THREE.Vector3) => void;
  updateEconomy: () => void;
  setBuildMode: (enabled: boolean) => void;
  setSelectedBuilding: (building: BuildingType | null) => void;
  updateCamera: (position: THREE.Vector3, target: THREE.Vector3) => void;
  setGameSpeed: (speed: number) => void;
  addSkier: () => void;
  updateSkierPositions: (deltaTime: number) => void;
  setTerrainData: (terrainData: THREE.BufferGeometry | null) => void;
}

export interface Building {
  id: string;
  type: BuildingType;
  position: THREE.Vector3;
  rotation: number;
  builtAt: Date;
}

export const useGameStore = create<GameState>((set, get) => ({
  // Initial state
  currentResort: {
    id: 'resort-1',
    name: 'Alpine Valley Resort',
    money: 100000,
    reputation: 50,
    guestCapacity: 100
  },
  
  terrainData: null,
  buildings: [],
  pistes: [],
  lifts: [],
  activeSkiers: [],
  
  isBuildMode: false,
  selectedBuildingType: null,
  cameraPosition: new THREE.Vector3(50, 50, 50),
  cameraTarget: new THREE.Vector3(0, 0, 0),
  
  economy: {
    currentMoney: 100000,
    dailyRevenue: 0,
    dailyExpenses: 0,
    guestSatisfaction: 75,
    totalSkiers: 0
  },
  
  isPaused: false,
  gameSpeed: 1,
  
  // Actions
  placeBuilding: (position: THREE.Vector3) => {
    const state = get();
    if (!state.selectedBuildingType) return;
    
    // Create a simple position object instead of cloning Vector3
    const newBuilding: Building = {
      id: `building-${Date.now()}`,
      type: state.selectedBuildingType,
      position: { x: position.x, y: position.y, z: position.z } as THREE.Vector3,
      rotation: 0,
      builtAt: new Date()
    };
    
    set(state => ({
      buildings: [...state.buildings, newBuilding],
      economy: {
        ...state.economy,
        currentMoney: state.economy.currentMoney - state.selectedBuildingType.cost,
        dailyExpenses: state.economy.dailyExpenses + state.selectedBuildingType.maintenanceCost
      }
    }));
  },
  
  createPiste: (nodes: THREE.Vector3[]) => {
    if (nodes.length < 2) return;
    
    const state = get();
    const pisteNodes: PisteNode[] = nodes.map((pos, index) => ({
      id: `node-${Date.now()}-${index}`,
      position: { x: pos.x, y: pos.y, z: pos.z } as THREE.Vector3,
      connections: index < nodes.length - 1 ? [`node-${Date.now()}-${index + 1}`] : []
    }));
    
    // Calculate difficulty based on slope angle
    const startElevation = nodes[0].y;
    const endElevation = nodes[nodes.length - 1].y;
    const horizontalDistance = Math.sqrt(
      Math.pow(nodes[nodes.length - 1].x - nodes[0].x, 2) + 
      Math.pow(nodes[nodes.length - 1].z - nodes[0].z, 2)
    );
    const slopeAngle = Math.atan2(startElevation - endElevation, horizontalDistance) * 180 / Math.PI;
    
    let difficulty: 'green' | 'blue' | 'black' = 'green';
    if (slopeAngle > 15) difficulty = 'black';
    else if (slopeAngle > 8) difficulty = 'blue';
    
    const totalLength = nodes.reduce((sum, node, index) => {
      if (index === 0) return 0;
      const prevNode = nodes[index - 1];
      const distance = Math.sqrt(
        Math.pow(node.x - prevNode.x, 2) + 
        Math.pow(node.y - prevNode.y, 2) + 
        Math.pow(node.z - prevNode.z, 2)
      );
      return sum + distance;
    }, 0);
    
    const newPiste: Piste = {
      id: `piste-${Date.now()}`,
      name: `Ski Run ${state.pistes.length + 1}`,
      nodes: pisteNodes,
      difficulty,
      length: totalLength,
      slopeAngle
    };
    
    set(state => ({
      pistes: [...state.pistes, newPiste]
    }));
  },
  
  createLift: (start: THREE.Vector3, end: THREE.Vector3) => {
    const state = get();
    
    // Create lift chairs
    const chairs: LiftChair[] = [];
    const numChairs = 20;
    for (let i = 0; i < numChairs; i++) {
      const progress = i / numChairs;
      chairs.push({
        id: `chair-${Date.now()}-${i}`,
        position: { 
          x: start.x + (end.x - start.x) * progress,
          y: start.y + (end.y - start.y) * progress,
          z: start.z + (end.z - start.z) * progress
        } as THREE.Vector3,
        progress: progress,
        occupied: false
      });
    }
    
    const newLift: Lift = {
      id: `lift-${Date.now()}`,
      name: `Lift ${state.lifts.length + 1}`,
      type: 'chairlift',
      startPosition: { x: start.x, y: start.y, z: start.z } as THREE.Vector3,
      endPosition: { x: end.x, y: end.y, z: end.z } as THREE.Vector3,
      capacity: numChairs,
      speed: 2.5,
      ticketPrice: 25,
      chairs
    };
    
    set(state => ({
      lifts: [...state.lifts, newLift]
    }));
  },
  
  updateEconomy: () => {
    const state = get();
    
    // Calculate daily revenue from multiple sources
    let totalRevenue = 0;
    
    // Lift ticket revenue
    const liftRevenue = state.lifts.reduce((sum, lift) => {
      return sum + (lift.chairs.filter(chair => chair.occupied).length * lift.ticketPrice);
    }, 0);
    totalRevenue += liftRevenue;
    
    // Building revenue (restaurants, shops, etc.)
    const buildingRevenue = state.buildings.reduce((sum, building) => {
      if (building.type.category === 'facility') {
        // Facilities generate revenue based on skier count
        const baseRevenue = building.type.maintenanceCost * 2; // Facilities should be profitable
        const skierMultiplier = Math.min(state.activeSkiers.length / 10, 2); // Max 2x multiplier
        return sum + Math.floor(baseRevenue * (0.5 + skierMultiplier));
      }
      return sum;
    }, 0);
    totalRevenue += buildingRevenue;
    
    // Calculate maintenance costs
    const maintenanceCosts = state.buildings.reduce((sum, building) => {
      return sum + building.type.maintenanceCost;
    }, 0);
    
    // Calculate lift operational costs
    const liftCosts = state.lifts.reduce((sum, lift) => {
      return sum + (lift.capacity * 5); // $5 per chair per day
    }, 0);
    
    const totalExpenses = maintenanceCosts + liftCosts;
    
    // Update guest satisfaction based on facilities and wait times
    let satisfaction = 75; // Base satisfaction
    
    // Bonus for having facilities
    const facilityCount = state.buildings.filter(b => b.type.category === 'facility').length;
    satisfaction += facilityCount * 5;
    
    // Bonus for lift capacity vs skier count
    const totalLiftCapacity = state.lifts.reduce((sum, lift) => sum + lift.capacity, 0);
    if (totalLiftCapacity > state.activeSkiers.length) {
      satisfaction += 10; // Good capacity
    } else {
      satisfaction -= 20; // Long wait times
    }
    
    // Penalty for having too few pistes
    if (state.pistes.length < 3) {
      satisfaction -= 15;
    }
    
    satisfaction = Math.max(0, Math.min(100, satisfaction));
    
    set(state => ({
      economy: {
        ...state.economy,
        dailyRevenue: totalRevenue,
        dailyExpenses: totalExpenses,
        currentMoney: state.economy.currentMoney + (totalRevenue - totalExpenses),
        guestSatisfaction: satisfaction
      }
    }));
  },
  
  setBuildMode: (enabled: boolean) => {
    set({ isBuildMode: enabled });
  },
  
  setSelectedBuilding: (building: BuildingType | null) => {
    set({ selectedBuildingType: building });
  },
  
  updateCamera: (position: THREE.Vector3, target: THREE.Vector3) => {
    set({
      cameraPosition: { x: position.x, y: position.y, z: position.z } as THREE.Vector3,
      cameraTarget: { x: target.x, y: target.y, z: target.z } as THREE.Vector3
    });
  },
  
  setGameSpeed: (speed: number) => {
    set({ gameSpeed: speed, isPaused: speed === 0 });
  },
  
  addSkier: () => {
    const state = get();
    if (state.activeSkiers.length >= state.currentResort!.guestCapacity) return;
    
    const skillLevels: ('beginner' | 'intermediate' | 'expert')[] = ['beginner', 'intermediate', 'expert'];
    const skillLevel = skillLevels[Math.floor(Math.random() * skillLevels.length)];
    
    const newSkier: SkierNPC = {
      id: `skier-${Date.now()}`,
      position: { x: Math.random() * 20 - 10, y: 0, z: Math.random() * 20 - 10 } as THREE.Vector3,
      skillLevel,
      currentRun: null,
      satisfaction: 80,
      moneySpent: 0,
      targetPosition: { x: 0, y: 0, z: 0 } as THREE.Vector3,
      speed: skillLevel === 'expert' ? 8 : skillLevel === 'intermediate' ? 6 : 4
    };
    
    set(state => ({
      activeSkiers: [...state.activeSkiers, newSkier],
      economy: {
        ...state.economy,
        totalSkiers: state.economy.totalSkiers + 1
      }
    }));
  },
  
  updateSkierPositions: (deltaTime: number) => {
    const state = get();
    if (state.isPaused) return;
    
    const updatedSkiers = state.activeSkiers.map(skier => {
      // Find nearest piste or lift
      let targetX = skier.targetPosition.x;
      let targetZ = skier.targetPosition.z;
      
      // If skier has no target, find a piste to ski down
      if (Math.abs(skier.position.x - targetX) < 2 && Math.abs(skier.position.z - targetZ) < 2) {
        // Find a suitable piste based on skill level
        const suitablePistes = state.pistes.filter(piste => {
          if (skier.skillLevel === 'beginner') return piste.difficulty === 'green';
          if (skier.skillLevel === 'intermediate') return piste.difficulty === 'green' || piste.difficulty === 'blue';
          return true; // Experts can ski anything
        });
        
        if (suitablePistes.length > 0) {
          const randomPiste = suitablePistes[Math.floor(Math.random() * suitablePistes.length)];
          if (randomPiste.nodes.length > 0) {
            const randomNode = randomPiste.nodes[Math.floor(Math.random() * randomPiste.nodes.length)];
            targetX = randomNode.position.x + (Math.random() - 0.5) * 10;
            targetZ = randomNode.position.z + (Math.random() - 0.5) * 10;
          }
        } else {
          // No pistes available, wander randomly
          targetX = (Math.random() - 0.5) * 100;
          targetZ = (Math.random() - 0.5) * 100;
        }
      }
      
      // Move toward target (skiing downhill)
      const dx = targetX - skier.position.x;
      const dz = targetZ - skier.position.z;
      const distance = Math.sqrt(dx * dx + dz * dz);
      
      if (distance < 0.5) {
        // Reached target, set new target
        return {
          ...skier,
          targetPosition: { x: targetX, y: 0, z: targetZ } as THREE.Vector3
        };
      }
      
      // Move downhill (slightly favor negative Z direction)
      const moveDistance = skier.speed * deltaTime * state.gameSpeed;
      const moveRatio = Math.min(moveDistance / distance, 1);
      
      // Add slight downhill bias
      const downhillBias = 0.1;
      const newZ = skier.position.z + dz * moveRatio + downhillBias * moveDistance;
      
      return {
        ...skier,
        position: {
          x: skier.position.x + dx * moveRatio,
          y: skier.position.y,
          z: newZ
        } as THREE.Vector3,
        targetPosition: { x: targetX, y: 0, z: targetZ } as THREE.Vector3
      };
    });
    
    // Update lift chairs
    const updatedLifts = state.lifts.map(lift => {
      const updatedChairs = lift.chairs.map(chair => {
        let newProgress = chair.progress + (lift.speed * deltaTime * state.gameSpeed * 0.01);
        
        if (newProgress >= 1) {
          newProgress = 0;
          // Randomly occupy/unoccupy chairs
          chair.occupied = Math.random() < 0.3;
        }
        
        // Calculate new position along lift line
        const newPos = {
          x: lift.startPosition.x + (lift.endPosition.x - lift.startPosition.x) * newProgress,
          y: lift.startPosition.y + (lift.endPosition.y - lift.startPosition.y) * newProgress,
          z: lift.startPosition.z + (lift.endPosition.z - lift.startPosition.z) * newProgress
        };
        
        return {
          ...chair,
          progress: newProgress,
          position: newPos as THREE.Vector3,
          occupied: chair.occupied
        };
      });
      
      return {
        ...lift,
        chairs: updatedChairs
      };
    });
    
    set({ 
      activeSkiers: updatedSkiers,
      lifts: updatedLifts
    });
  },
  
  setTerrainData: (terrainData: THREE.BufferGeometry | null) => {
    set({ terrainData });
  }
}));