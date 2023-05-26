function GatherTheResourcesCondition(const state: TState): boolean;
begin
  Result := True;
end;

procedure FindAndLoot(Callback: Procedure(ID: Cardinal); const items: Array of Cardinal);
var 
  i: Integer;
begin  
  if Length(items) > 0 then
    for i := 0 to High(items) do
      begin
        if Not NewMoveXY(GetX(items[i]), GetY(items[i]), true, 1, true) then
          begin
            AddToSystemJournal('[Critical] Resource is not reachable X: ', GetX(items[i]), ' Y: ', GetY(items[i]));
            Continue;
          end;
        // could be that somebody else took the thing
        if Not IsObjectExists(items[i]) then
          begin
            AddToSystemJournal('[Warning] Resource not longer Exists!');
            Continue;
          end;
      Callback(items[i]);
    end;
end;

procedure MoveItemById(item: Cardinal);
var  lootItems: Array of Cardinal;
begin
  MoveItem(item, 0, Backpack, 0,0,0);
  Wait(1500);
end;

procedure LootGatheredMaterial(item: Cardinal);
var  lootItems: Array of Cardinal;
begin
  UseObject(item);
  Wait(1000);

  FindTypesArrayEx(LOOT_TYPES, [$FFFF], [Ground], false);
  GetFoundItemsFromStringList(lootItems);
  FindAndLoot(@MoveItemById, lootItems);  
end;

procedure GatherTheResourcesExecutor(var state: TState; const command: TCommand);
var 
  fdv: Cardinal;
  items: Array of Cardinal; 
begin
  fdv:= FindDistance;
  FindDistance := 20;
  FindTypesArrayEx(state.GatherTypes, [$FFFF], [Ground], false);
  GetFoundItemsFromStringList(items);
  FindAndLoot(@LootGatheredMaterial, items);
  FindDistance := fdv;
end;

function GatherTheResourcesAction(): TCommand;
begin
  Result.Title := 'Gather the resources';
  Result.Condition := @GatherTheResourcesCondition;
  Result.Executor := @GatherTheResourcesExecutor;
  Result.Priority := LowPrio;
end;