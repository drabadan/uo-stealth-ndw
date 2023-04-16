var DEFAULT_ALCHEMY_TOOL_TYPES: array of Word = [$1849, $184B, $184A, $184C];
const ALCHEMY_MENU_STRING = 'Make what?';

function LesserStrengthPotion: TCraftable;
begin
    Result.Title := 'lesser strength potion';
    Result.Reagents.SetLength(1);
    Result.Reagents[0].Graphic := MR;
    Result.Reagents[0].Quantity := 1;
    Result.Reagents[0].Title := 'Mandrake root';
    Result.Crafting.ToolGraphics := DEFAULT_ALCHEMY_TOOL_TYPES;
    Result.Crafting.ToolTarget := MR;
    Result.Crafting.MainMenuString := ALCHEMY_MENU_STRING;
    Result.Crafting.MenuChoice := 'lesser strength potion';
end;

function DeadlyPoisonPotion: TCraftable;
begin
    Result.Title := 'deadly poison potion';
    Result.Reagents.SetLength(1);
    Result.Reagents[0].Graphic := NS;
    Result.Reagents[0].Quantity := 10;
    Result.Reagents[0].Title := 'Nightshade';
    Result.Crafting.ToolGraphics := DEFAULT_ALCHEMY_TOOL_TYPES;
    Result.Crafting.ToolTarget := NS;
    Result.Crafting.MainMenuString := ALCHEMY_MENU_STRING;
    Result.Crafting.MenuChoice := 'a deadly poison potion';
end;