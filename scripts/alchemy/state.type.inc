type 
    TState = record
        RunsCount: Integer;
        CurrentCraftable: TCraftable;
        CurrentPouringContainerId: Cardinal;
        PouringContainerGraphic: Word;        
        MinPerRegQuantity, LoadRegsQuantity: Integer;
        PouringContainerContainer: Cardinal;
    end;