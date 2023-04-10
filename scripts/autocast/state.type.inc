type 
    TBuffTarget = record
        Serial: Cardinal;
        SenderName: String;
        Phrase: string;
    end;
    TBuffTargetArray = Array of TBuffTarget;
    TState = record
        BuffTargets: TBuffTargetArray;
    end;