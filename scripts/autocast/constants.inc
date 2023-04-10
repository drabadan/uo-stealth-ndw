const SPEECH_PATTERNS = ['0', '1', '01', '2', '12', '012', '3', '123', '0123', '4', '6', '666', '5', 'Каст', 'Себя'];
// Matches index in array with Speech pattern

const SPELLS_MATCH = [
    // 0
    'Dispel', 
    // 1
    'Bless', 
    // 2
    'Strength,Cunning,Agility', 
    // 3
    'Spell Reflection,Protection,Reactive Armor', 
    // 4
    'Spell Reflection', 
    // 5
    'Greater Heal', 
    // 6
    'Protection',
    // 666
    'Dispel,Bless,Strength,Agility,Cunning',
    // Каст
    'Dispel,Bless,Strength,Agility,Cunning,Protection',
    // 'Себя':
    'Dispel,Bless,Strength,Agility,Cunning,Protection'
];