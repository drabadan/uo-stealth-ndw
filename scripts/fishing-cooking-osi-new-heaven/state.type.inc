type
  TLootShop = record
    VendorName: String;
    VendorId: Cardinal;
    LocationPoint: TPoint;
    SellTypes: Array of Word;
  end;
type 
  TState = record
    RunsCount: Integer;
    FishingPoint: TPoint;
    ForgePoint: TPoint;
    CookingSteaksQuantityFlag: Word;
    GoSellLootFlag: Word;
    LootShops: Array of TLootShop;
    AllLootTypesToSell: Array of Word;
  end;