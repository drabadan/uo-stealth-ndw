type
  TField = record
    CenterPoint: TPoint;
  end;

type 
  TState = record
    RunsCount: Integer;
    Fields: Array of TField;
    CurrentFieldIndex: Integer;
    UnloadQuantity: Word;
    GatherTypes: Array of Word;
    LootTypes: Array of Word;
    FlourMillPoint: TPoint;
    UnloadChestId: Cardinal;
    Run: boolean;
  end;