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

{
* Info * : ID: $5ACA2382 Name:  Type: $0E29 Color: $0000 
* Info * : Quantity: 1 X: 5998 Y: 1232 Z: 5
* Info * : Tooltip: an invisibility potion bottled by Dani
}
function InvisibilityPotion: TCraftable;
begin
    Result.Title := 'invisibility potion';
    Result.Reagents.SetLength(2);
    //ExCap
    Result.Reagents[0].Graphic := $0F83;
    Result.Reagents[0].Quantity := 3;
    Result.Reagents[0].Title := 'ExCap';
    Result.Reagents[1].Graphic := NS;
    Result.Reagents[1].Quantity := 1;
    Result.Reagents[1].Title := 'Nightshade';
    Result.Crafting.ToolGraphics := DEFAULT_ALCHEMY_TOOL_TYPES;
    Result.Crafting.ToolTarget := NS;
    Result.Crafting.MainMenuString := ALCHEMY_MENU_STRING;
    Result.Crafting.MenuChoice := 'a invisibility potion';
end;