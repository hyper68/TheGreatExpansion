import { BuildingType } from '../stores/gameStore';

export const BUILDING_TYPES: BuildingType[] = [
  // Lifts
  {
    id: 'chairlift-small',
    name: 'Small Chairlift',
    cost: 50000,
    maintenanceCost: 500,
    modelPath: '/models/lifts/chairlift-small.glb',
    category: 'lift'
  },
  {
    id: 'chairlift-medium',
    name: 'Medium Chairlift',
    cost: 100000,
    maintenanceCost: 800,
    modelPath: '/models/lifts/chairlift-medium.glb',
    category: 'lift'
  },
  {
    id: 'gondola',
    name: 'Gondola',
    cost: 200000,
    maintenanceCost: 1500,
    modelPath: '/models/lifts/gondola.glb',
    category: 'lift'
  },
  
  // Buildings
  {
    id: 'lodge-small',
    name: 'Small Lodge',
    cost: 25000,
    maintenanceCost: 200,
    modelPath: '/models/buildings/lodge-small.glb',
    category: 'building'
  },
  {
    id: 'lodge-large',
    name: 'Large Lodge',
    cost: 60000,
    maintenanceCost: 400,
    modelPath: '/models/buildings/lodge-large.glb',
    category: 'building'
  },
  {
    id: 'restaurant',
    name: 'Restaurant',
    cost: 40000,
    maintenanceCost: 300,
    modelPath: '/models/buildings/restaurant.glb',
    category: 'building'
  },
  {
    id: 'rental-shop',
    name: 'Rental Shop',
    cost: 15000,
    maintenanceCost: 100,
    modelPath: '/models/buildings/rental-shop.glb',
    category: 'building'
  },
  
  // Facilities
  {
    id: 'snow-cannon',
    name: 'Snow Cannon',
    cost: 8000,
    maintenanceCost: 50,
    modelPath: '/models/facilities/snow-cannon.glb',
    category: 'facility'
  },
  {
    id: 'parking-lot',
    name: 'Parking Lot',
    cost: 10000,
    maintenanceCost: 25,
    modelPath: '/models/facilities/parking-lot.glb',
    category: 'facility'
  },
  {
    id: 'rest-area',
    name: 'Rest Area',
    cost: 5000,
    maintenanceCost: 20,
    modelPath: '/models/facilities/rest-area.glb',
    category: 'facility'
  }
];

export const getBuildingType = (id: string): BuildingType | undefined => {
  return BUILDING_TYPES.find(type => type.id === id);
};

export const getBuildingTypesByCategory = (category: 'lift' | 'building' | 'facility'): BuildingType[] => {
  return BUILDING_TYPES.filter(type => type.category === category);
};