function SellFishingLootCondition(const state: TState): boolean;
begin
  FindTypesArrayEx(state.AllLootTypesToSell, [$FFFF], [Backpack], false);
  AddToSystemJournal('Found Loot Items ', FindCount);
  Result := FindCount >= state.GoSellLootFlag;
end;

procedure SellFishingLootExecutor(var state: TState; const command: TCommand);
var
  i, k: Integer;
begin
  AddToSystemJournal('[INFO] Going Selling loot!');

  for k:= 0 to High(state.LootShops) do
    begin
      FindTypesArrayEx(state.LootShops[k].SellTypes, [$FFFF], [Backpack], false);
      if FindCount = 0 then Continue;

      NewMoveXY(state.LootShops[k].LocationPoint.X, state.LootShops[k].LocationPoint.Y, true, 1, true);
      NewMoveXY(GetX(state.LootShops[k].VendorId), GetY(state.LootShops[k].VendorId), true, 1, true);
      Wait(500);
      for i := 0 to High(state.LootShops[k].SellTypes) do
        AutoSell(state.LootShops[k].SellTypes[i], $FFFF, 9999);
    
      UoSay(state.LootShops[k].VendorName + ' sell');
      Wait(3000);
    
      for i := 0 to High(state.LootShops[k].SellTypes) do
        AutoSell(state.LootShops[k].SellTypes[i], $FFFF, 0);
    end;
end;

function SellFishingLootAction(): TCommand;
begin
  Result.Title := 'Sell Fishing Loot';
  Result.Condition := @SellFishingLootCondition;
  Result.Executor := @SellFishingLootExecutor;
  Result.Priority := HighPrio;
end;