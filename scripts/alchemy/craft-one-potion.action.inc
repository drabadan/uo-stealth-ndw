function CraftOnePotionCondition(const state: TState): boolean;
begin
  Result := True;
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

  WaitMenu(craftConfig.MainMenuString, craftConfig.MenuChoice);
  WaitGump('0');
  WaitTargetObject(state.CurrentPouringContainerId);
  AddToSystemJournal('CurrentPouringContainerId: ' + state.CurrentPouringContainerId.toString());
  UseObject(FindItem);
  if not WaitJournalLineSystem(Now, StrJoin([ALCHEMY_CRAFTING_RETRY_MSG_STRING, ALCHEMY_CRAFTING_FINISH_MSG_STRING], '|'), MAX_CRAFT_WAIT_TIME_MS) then
    AddToSystemJournal('[Critical] Has not received any messages from crafting ' + state.CurrentCraftable.Title);
end;

function CraftOnePotionAction(): TCommand;
begin
  Result.Title := 'Craft one potion';
  Result.Condition := @CraftOnePotionCondition;
  Result.Executor := @CraftOnePotionExecutor;
  Result.Priority := LowPrio;
end;