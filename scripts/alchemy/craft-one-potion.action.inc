function CraftOnePotionCondition(const state: TState): boolean;
begin
  Result := True;
end;

function AwaitForSomethingBoolean(Something: Function: boolean): boolean;
var i: Integer;
begin
  for i:= 0 to 300 do
    begin
      Wait(100);
      if Something() then Exit(True);
    end;

  Exit(False);
end;

procedure CraftOnePotionExecutor(var state: TState; const command: TCommand);
var
  craftConfig: TCraftConfig;
begin
  craftConfig := state.CurrentCraftable.Crafting;
  FindTypesArrayEx(craftConfig.ToolGraphics, [$FFFF], [backpack], false);
  if (FindItem = 0) then 
    begin
      AddToSystemJournal('[Critical] Cannot find any tools to craft ' + state.CurrentCraftable.Title);
      Exit;
    end;

  WaitGump('0');  
  
  AddToSystemJournal(craftConfig.MainMenuString  + ' ' + craftConfig.MenuChoice);
  WaitTargetType(state.CurrentCraftable.Reagents[0].Graphic);
  WaitMenu(craftConfig.MainMenuString, craftConfig.MenuChoice);
  UseObject(FindItem);  
  if Not AwaitForSomethingBoolean(@TargetPresent) then
    begin
      AddToSystemJournal('[Critical] Target not present after Reg Selected');
      Exit;
    end;

  TargetToObject(state.CurrentPouringContainerId);
  WaitJournalLineSystem(
    Now, 
    StrJoin([ALCHEMY_CRAFTING_RETRY_MSG_STRING, ALCHEMY_CRAFTING_FINISH_MSG_STRING], '|'), 
    MAX_CRAFT_WAIT_TIME_MS);
end;

function CraftOnePotionAction(): TCommand;
begin
  Result.Title := 'Craft one potion';
  Result.Condition := @CraftOnePotionCondition;
  Result.Executor := @CraftOnePotionExecutor;
  Result.Priority := LowPrio;
end;