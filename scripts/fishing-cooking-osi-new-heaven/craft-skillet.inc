function CraftSkilletCondition(const state: TState): boolean;
begin
  FindType(SKILLET_TYPE, Backpack);
  Result := FindCount <= 2;
end;

function FindTool(ToolType: Word): Cardinal;
begin
  if FindType(ToolType, Backpack) = 0 then
    begin
      AddToSystemJournal('[Critical] Tinker tools not found!');
      Exit;
    end;

  if FindCount < 3 then AddToSystemJournal('[Warning] Low on tinker tools: ', FindCount);
  Result := FindItem;
end;

procedure GetTinkeringGump(GumpSerial: Cardinal);
var
  i: Integer;
begin
  for i := 0 to GetGumpsCount-1 do
    if (GetGumpSerial(i) = GumpSerial) then Exit else CloseSimpleGump(i);      
 
  UseObject(GumpSerial);
  if NOT WaitGumpMaxMS(3000, COOKING_GUMP_ID) then AddToSystemJournal('[Critical] No Craft gump present!');  
end;

procedure CraftSkilletExecutor(var state: TState; const command: TCommand);
begin
  AddToSystemJournal('[INFO] Crafting skillets!');
  repeat
    FindType(IRON_INGOT_TYPE, Backpack);
    if FindFullQuantity < 80 then AddToSystemJournal('[Warning] Low on Iron ingots');       
    GetTinkeringGump(FindTool(TINKER_TOOLS));
    NumGumpButton(GetGumpsCount-1, TINKERING_SKILLET_NUM_GUMP_BUTTON);
    WaitGumpMaxMS(5000, COOKING_GUMP_ID);
    FindType(SKILLET_TYPE, Backpack);
  until FindCount > 3;

  Wait(3000);
  CloseAllGumps();
end;

function CraftSkilletAction(): TCommand;
begin
  Result.Title := 'Cook fish steak';
  Result.Condition := @CraftSkilletCondition;
  Result.Executor := @CraftSkilletExecutor;
  Result.Priority := HighPrio;
end;