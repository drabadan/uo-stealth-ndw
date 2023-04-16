function SetCurrentPouringContainerCondition(const state: TState): boolean;
begin
  Result := state.CurrentPouringContainerId = 0;
end;

function FindMyKeg(Container: Cardinal; state: TState): Cardinal;
var
  i: Integer;
  tooltip: String;
  items: Array Of Cardinal;
begin
  if ((Container <> Ground) AND (Container <> Backpack)) then
    begin
      newMoveXY(GetX(Container), GetY(Container), true, 1, true);
      UseObject(Container);
      Wait(1000);
    end;
  
  FindType(KEG_GRAPHIC, Container);
  if (GetFoundItemsFromStringList(items) = 0) then
    begin
      AddToSystemJournal('[Critical] No kegs found in Container');
      Exit;
    end;

  for i := items.Low to items.High do 
    begin
      tooltip := GetTooltip(items[i]);
      if ((StrFind(tooltip, 'by ' + CharName) > 0) AND (StrFind(tooltip, state.CurrentCraftable.Title) > 0)) then
        begin
          AddToSystemJournal('[Info] Found pouring container for ' + state.CurrentCraftable.Title);
          Result := items[i];
          Exit;
        end;
    end;

  AddToSystemJournal('[Critical] Cannot find pouring container.');
end;

procedure SetCurrentPouringContainerExecutor(var state: TState; const command: TCommand);
begin
  if (state.PouringContainerGraphic = KEG_GRAPHIC) then
    state.CurrentPouringContainerId := FindMyKeg(Backpack, state)
  else 
    begin
      FindTypesArrayEx([$0F0E], [$0000], [backpack, Ground], false);
      if FindCount > 0 then         
        state.CurrentPouringContainerId := FindItem
      else 
        AddToSystemJournal('[Critical] Cannot find empty bottles.');
    end;   
  // gState := state;
  AddToSystemJournal(state.CurrentPouringContainerId);
end;

function SetCurrentPouringContainerAction(): TCommand;
begin
  Result.Title := 'Set current pouring container';
  Result.Condition := @SetCurrentPouringContainerCondition;
  Result.Executor := @SetCurrentPouringContainerExecutor;
  Result.Priority := LowPrio;
end;