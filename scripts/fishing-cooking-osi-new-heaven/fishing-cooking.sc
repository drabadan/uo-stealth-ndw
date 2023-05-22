Program FishingCooking;

{$REGION 'Includes'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}
{$I 'state.type.inc'}
{$I 'constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
{$I 'actions.inc'}
{$ENDREGION}

function InitLootShops: Array of TLootShop;
var tmpShop: TLootShop;
begin    
  // Cobbler New Heaven
  tmpShop.VendorName := 'Catherine';
  tmpShop.VendorId := $AE5795;
  tmpShop.LocationPoint.X := 3496;
  tmpShop.LocationPoint.Y := 2552;
  tmpShop.SellTypes := SHOES_FROM_FISHING;
  Result.Add(tmpShop);

  // Fisher New Heaven
  tmpShop.VendorName := 'Kurt';
  tmpShop.VendorId := $AEB03A;
  tmpShop.LocationPoint.X := 3518;
  tmpShop.LocationPoint.Y := 2594;
  tmpShop.SellTypes := PRIZED_FISHES;
  Result.Add(tmpShop);
end;

function GetInitialState(): TState;
begin  
  Result.RunsCount := 0;
  Result.FishingPoint.X := 3500;
  Result.FishingPoint.Y := 2609;
  Result.CookingSteaksQuantityFlag := 80;
  Result.ForgePoint.X := 3525;
  Result.ForgePoint.Y := 2536;
  Result.GoSellLootFlag := 25;
  Result.LootShops := InitLootShops();
  Result.AllLootTypesToSell := SHOES_FROM_FISHING + PRIZED_FISHES;
end;

begin
  MoveOpenDoor := true;
  gState := GetInitialState();
  RegisterActions([    
    SellFishingLootAction(),
    CraftSkilletAction(),
    CookFishSteakAction(),
    GetRawFishSteaksAction()
  ]);
  RunUntilCancel(gState);
end.