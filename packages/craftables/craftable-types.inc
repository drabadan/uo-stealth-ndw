type
    TReagent = record
        Graphic: Word;
        Quantity: Word;
        Title: String;
    end;
    TReagentsArray = Array of TReagent;
    TCraftConfig = record
        ToolGraphics: Array of Word;
        ToolTarget: Word;
        MainMenuString: String;
        MenuChoice: String;
    end;
    TCraftable = record
        // Lowercase full name of potion
        // Important for pouring into Kegs to find proper keg
        Title: String;
        Reagents: TReagentsArray;
        Graphic: Word;
        Crafting: TCraftConfig;
    end;
