program Alchemy;

var
  messtype: array of word;

const
  {---* Script Setup *---}
  NeedName = 'lesser cure potion';
  GrabCnt = 50;// по сколько берем
  UseKeg = False; //используем ли кег
  chest2 = $43005E9F; {Указать ID сундука с полными сумками}
  chest1 = $43010169; {Указать ID сундука с пустыми сумками}
  trashtype = $0f0a;
  bottleless = $1940;  //кеги
  //Box = $41F738AC;   // Коробка  с продукцией
  Box = $41F738AC;   // Коробка  с продукцией
  {----* End Setup *----}
  NameMenu = 'wish to make?';  // менюшка алхимии
  HstandType = $1849;
  fHstandType = $184B;
  ffHstandType = $184A;
  fffHstandType = $184C;
  emptykegType = $1940;
  emptybottleType = $0F0E;
  ExCap = $0F83;
  DBlood = $0F82;
  GSP = $0F09;
  SScal = $0F8E;
  BMoor = $0F79;
  Rubiesy = $0F13;
  Amethysts = $0F16;
  GHP = $0F0C;
  WH  = $0F91;
  SSapph = $0F0F;
  Diamonds = $0F30;
  Batwings = $0F78;
  DBones = $0F80;
  GCP = $0F07;
  Pumice = $0F8B;
  Emerald = $0F10;
  FDirt = $0F81;
  vAsh = $0F8F;
  dWood = $0F90;
  lExP = $0F0D;
  bones = $0F7E;
  pIrons = $0F8A;
  eoN = $0F87;
  obsidians = $0F89;
  sof = $1045;
  vob = $0F7D;

{$Include 'all.inc'}

  procedure FullDrop;
  var
    Cnt, d: integer;
  begin

    messtype := [$0F07, $0F0D, $0F0C, $0f0A];
    wait(1000);
    WaitConnection(3000);
    CheckSave;
    for d := 0 to getArrayLength(messtype) - 1 do
    begin
      while Count(messtype[d]) > 0 do
      begin
        //Addtosystemjournal(inttostr(d)+' выкидываем мусор');
        //MoveItem(findtype(messtype[d],backpack),1,Box,0,0,0);
        MoveItem(findtype(messtype[d], backpack), 1, ground, 0, 0, 0);
        wait(100);
      end;
    end;
    Cnt := Count(bottleless);
    if Cnt > 0 then
    begin
      for d := 1 to Cnt do
      begin
        FindType(bottleless, backpack);
        if FindCount > 0 then
        begin
          MoveItem(finditem, 0, Box, 0, 0, 0);
          wait(1000);
        end;
      end;
    end;
    stack($0F7F, $0000);
  end;

  procedure ConfigNeedReagent(list: integer; UseKeg: boolean);
  var
    menu: boolean;
    needregCnt, r, bpCnt, ngCnt, a, b, c: integer;
    needreg: array [1..15] of word;
    needquant: array of byte;
    HeatStand, TGrabCnt, flacon, material: cardinal;
    flaconType: word;
    flaconCnt: byte;
    stime, ftime: TDateTime;
  begin
    if (list = 1) or (list = 10) or (list = 16) then
    begin      //Strenght
      if list >= 1 then
      begin
        menu := True;
      end;
      needregCnt := 1;
      needreg[1] := MR;
      if list = 1 then
        needquant := [1];
      if list = 10 then
        needquant := [3];
      if list = 16 then
        needquant := [7];
    end else
    begin
      if (list = 2) or (list = 9) or (list = 15) then
      begin     //Agilirt
        if list >= 2 then
        begin
          menu := True;
        end;
        needregCnt := 1;
        needreg[1] := BM;
        if list = 2 then
          needquant := [1];
        if list = 9 then
          needquant := [3];
        if list = 15 then
          needquant := [9];
      end else
      begin
        if (list = 3) or (list = 13) then
        begin              //Refresh
          if list >= 3 then
          begin
            menu := True;
          end;
          needregCnt := 1;
          needreg[1] := BP;
          if list = 3 then
            needquant := [1];
          if list = 13 then
            needquant := [9];
        end else
        begin
          if list = 4 then
          begin              //night sight
            menu := True;
            needregCnt := 1;
            needreg[1] := SS;
            if list = 4 then
              needquant := [3];
          end else
          begin
            if (list = 5) or (list = 12) or (list = 18) then
            begin    //cure
              if list >= 5 then
              begin
                menu := True;
              end;
              needregCnt := 1;
              needreg[1] := GA;
              if list = 5 then
                needquant := [1];
              if list = 12 then
                needquant := [4];
              if list = 18 then
                needquant := [9];
            end else
            begin
              if (list = 6) or (list = 14) or (list = 19) then
              begin  //heal
                if list >= 6 then
                begin
                  menu := True;
                end;
                needregCnt := 1;
                needreg[1] := GS;
                if list = 6 then
                  needquant := [1];
                if list = 14 then
                  needquant := [4];
                if list = 19 then
                  needquant := [9];
              end else
              begin
                if (list = 7) or (list = 11) or (list = 20) or (list = 24) then
                begin  //poison
                  if list >= 7 then
                  begin
                    menu := True;
                  end;
                  needregCnt := 1;
                  needreg[1] := NS;
                  if list = 7 then
                    needquant := [1];
                  if list = 11 then
                    needquant := [4];
                  if list = 20 then
                    needquant := [12];
                  if list = 24 then
                    needquant := [20];
                end else
                begin
                  if (list = 8) or (list = 17) or (list = 22) then
                  begin //exlosion
                    if list >= 8 then
                    begin
                      menu := True;
                    end;
                    needregCnt := 1;
                    needreg[1] := SA;
                    if list = 8 then
                      needquant := [3];
                    if list = 17 then
                      needquant := [6];
                    if list = 22 then
                      needquant := [20];
                  end else
                  begin
                    if list = 21 then
                    begin      //invis
                      menu := True;
                      needregCnt := 2;
                      needreg[1] := ExCap;
                      needreg[2] := NS;
                      if list = 21 then
                        needquant := [3, 1];
                    end else
                    begin
                      if (list = 23) or (list = 27) then
                      begin  //homeric
                        if list >= 23 then
                        begin
                          menu := True;
                        end;
                        needregCnt := 2;
                        needreg[1] := DBlood;
                        needreg[2] := GSP;
                        if list = 23 then
                          needquant := [1, 1];
                        if list = 27 then
                          needquant := [4, 1];
                      end else
                      begin
                        if (list = 25) or (list = 28) or (list = 30) then
                        begin     //invul
                          if list >= 25 then
                          begin
                            menu := True;
                          end;
                          needregCnt := 2;
                          needreg[1] := SScal;
                          needreg[2] := GS;
                          if list = 25 then
                            needquant := [1, 1];
                          if list = 28 then
                            needquant := [4, 3];
                          if list = 30 then
                            needquant := [6, 5];
                        end else
                        begin
                          if list = 26 then
                          begin                //mana refresh
                            needregCnt := 3;
                            needreg[1] := BMoor;
                            needreg[2] := Rubiesy;
                            needreg[3] := Amethysts;
                            if list = 26 then
                              needquant := [15, 5, 3];
                          end else
                          begin
                            if list = 29 then
                            begin                //tamla heal
                              needregCnt := 3;
                              needreg[1] := GHP;
                              needreg[2] := SSapph;
                              needreg[3] := WH;
                              if list = 29 then
                                needquant := [1, 4, 3];
                            end else
                            begin
                              if list = 31 then
                              begin              //elixir
                                needregCnt := 2;
                                needreg[1] := WH;
                                needreg[2] := Diamonds;
                                if list = 31 then
                                  needquant := [1, 1];
                              end else
                              begin
                                if list = 32 then
                                begin
                                  needregCnt := 4;
                                  needreg[1] := Batwings;
                                  needreg[2] := DBones;
                                  needreg[3] := WH;
                                  needreg[4] := Diamonds;
                                  if list = 32 then
                                    needquant := [2, 5, 6, 5];
                                end else
                                begin
                                  if list = 33 then
                                  begin
                                    needregCnt := 4;
                                    needreg[1] := GCP;
                                    needreg[2] := Pumice;
                                    needreg[3] := DBones;
                                    needreg[4] := Emerald;
                                    if list = 33 then
                                      needquant := [1, 1, 4, 1];
                                  end else
                                  begin
                                    if (list = 34) or (list = 35) or (list = 36) then
                                    begin
                                      if list >= 34 then
                                      begin
                                        menu := True;
                                      end;
                                      needregCnt := 2;
                                      needreg[1] := FDirt;
                                      needreg[2] := vAsh;
                                      if list = 34 then
                                        needquant := [1, 1];
                                      if list = 35 then
                                        needquant := [2, 3];
                                      if list = 36 then
                                        needquant := [2, 7];
                                    end else
                                    begin
                                      if (list = 37) or (list = 38) then
                                      begin
                                        if list >= 37 then
                                        begin
                                          menu := True;
                                        end;
                                        needregCnt := 2;
                                        needreg[1] := FDirt;
                                        needreg[2] := dWood;
                                        if list = 37 then
                                          needquant := [2, 3];
                                        if list = 38 then
                                          needquant := [5, 3];
                                      end else
                                      begin
                                        if list = 39 then
                                        begin
                                          needregCnt := 3;
                                          needreg[1] := lExP;
                                          needreg[2] := bones;
                                          needreg[3] := pIrons;
                                          if list = 39 then
                                            needquant := [1, 2, 2];
                                        end else
                                        begin
                                          if list = 40 then
                                          begin
                                            needregCnt := 3;
                                            needreg[1] := SS;
                                            needreg[2] := eoN;
                                            needreg[3] := obsidians;
                                            if list = 40 then
                                              needquant := [1, 3, 3];
                                          end else
                                          begin
                                            if list = 41 then
                                            begin
                                              needregCnt := 5;
                                              needreg[1] := sof;
                                              needreg[2] := dBlood;
                                              needreg[3] := fdirt;
                                              needreg[4] := vob;
                                              needreg[5] := dWood;
                                              if list = 41 then
                                                needquant := [10, 10, 20, 10, 10];
                                            end else
                                            begin
                                              AddToSystemJournal('Произошла ошибка скрипта. Проверьте настройки');
                                              wait(500);
                                            end;
                                          end;
                                        end;
                                      end;
                                    end;
                                  end;
                                end;
                              end;
                            end;
                          end;
                        end;
                      end;
                    end;
                  end;
                end;
              end;
            end;
          end;
        end;
      end;
    end;

    if UseKeg then
    begin
      flaconType := emptykegType;
      wait(500);
      AddToSystemJournal('используем кеги');
    end else
    begin
      flaconType := emptybottleType;
      wait(500);
      AddToSystemJournal('используем бутылки');
    end;
    flaconCnt := CountGround(flaconType);
    if flaconCnt > 0 then
    begin
      AddToSystemJournal('на полу тары: ' + IntToStr(flaconCnt));
    end else
    begin
      AddToSystemJournal('На земле тары нет. ждем');
      //Setarstatus(false);
      //disconnect;
      wait(60000);
      //SetArstatus(true);
      //AddToSystemJournal(' конектимся.')
      Exit;
    end;
    {While (not Dead) do }begin
      {подбор реагентов}
      for r := 1 to needregCnt do
      begin
        WaitConnection(3000);
        CheckSave;
        bpCnt := Count(needreg[r]);
        TGrabCnt := (GrabCnt * (needquant[r - 1]));
        while (bpCnt < TGrabCnt) do
        begin
          ngCnt := (TGrabCnt - bpCnt);
          FindType(needreg[r], ground);
          if FindCount > 0 then
          begin
            Grab(finditem, ngCnt);
            bpCnt := Count(needreg[r]);
          end else
          begin
            AddToSystemJournal(' Реагент № ' + IntToStr(r) + ' закончился');
            wait(60000);
            exit;
          end;
          //if FindCount > 25 then begin
          //MoveItems (ground,needreg[r],getcolor(findtype(needreg[r],ground)),backpack,0,0,0,200);
          //checklag
          //bpCnt := Count(needreg[r]);
          //AddtoSystemJournal(IntToStr(CountGround(needreg[r])));
          //end
        end;
        AddtoSystemJournal(IntToStr(CountGround(needreg[r])));
      end;
      {--------------------------}
      wait(1000);
      WaitConnection(3000);
      CheckSave;
      FindType(HstandType, backpack);
      if FindCount > 0 then
      begin
        HeatStand := finditem;
      end else
      begin
        wait(500);
        FindType(fHstandType, backpack);
        if FindCount > 0 then
        begin
          HeatStand := finditem;
        end else
        begin
          wait(500);
          FindType(ffHstandType, backpack);
          if FindCount > 0 then
          begin
            HeatStand := finditem;
          end else
          begin
            FindType(fffHstandType, backpack);
            if FindCount > 0 then
            begin
              HeatStand := finditem;
            end else
            begin
              AddToSystemJournal('Закончились керосинки');
              //FullDisconnect;
            end;
          end;
        end;
      end;
      WaitConnection(3000);
      CheckSave;
      FindType(flaconType, backpack);
      if findcount = 0 then
      begin
        FindType(flaconType, ground);
        if FindCount > 0 then
        begin
          grab(finditem, GrabCnt);
          findType(flaconType, backpack);
          flacon := finditem;

        end else
        begin
          AddToSystemJournal('Закончилась тара. ждемс минуту.');
          wait(60000);
          exit;
        end;
      end else
      begin
        flacon := finditem;
        AddToSystemJournal('В портфеле емкость, льем в нее');
      end;
      wait(500);
      WaitConnection(3000);
      CheckSave;
      FindType(needreg[1], backpack);
      if FindCount > 0 then
      begin
        material := finditem;
        AddToSystemJournal('Начинаем процесс изготовления');
        if TargetPresent then
        begin
          CancelTarget;
          wait(500);
        end;
        Wait(500);
        WaitConnection(5000);
        CheckSave;
        CancelMenu;
        stime := now;
        WaitTargetObject(material);
        UseObject(HeatStand);
        if menu then
        begin
          AddToSystemJournal('выбор в меню');
          WaitMenu(NameMenu, NeedName);
        end;
        repeat
          a := InJournalBetweenTimes('bottle or keg', stime, Now);
          wait(500);
        until (a >= 0) or (Now > stime + (1.0 / 1440));
        if a >= 0 then
        begin
          WaitTargetObject(flacon);
          WaitGump('1');
          AddToSystemJournal('Пошла родная');
          uosay('Пошла родная');
          ftime := now;
          WaitConnection(3000);
          repeat
            Wait(200);
            //uosay(inttostr(c));
            CheckSave;
            b := InJournalBetweenTimes('out of|make anything|required materials"', ftime, Now);
            c := InJournalBetweenTimes('already has another|already full|mix|stop', ftime, Now);
            if (now > ftime + (3.0 / 1440)) then
            begin
              UoSay('.online');
              wait(100);
              ftime := now;
            end;
            wait(500);
          until (b >= 0) or (c >= 0) {or ((usekeg=false) and (Count(emptybottleType)=0))} or
            (now > stime + 10.0 / 1440) or (not Connected);
          WaitConnection(3000);
          if (c >= 0) then
          begin
            //uosay(inttostr(c));
            //uosay('вышел по с');
            //Addtosystemjournal(Journal(c));
            FullDrop;
          end;
          if ((b >= 0) and ((List = 32) or (List = 41))) then
          begin
            uosay('вышел по в');
            FullDrop;
          end;
          if TargetPresent then
          begin
            CancelTarget;
            wait(500);
          end;
        end;
        Hungry(1, -1);
      end;
    end;
  end;

  procedure AutoSetup;
  var
    name: array [1..41] of string;
    i: integer;
  begin
    name[1]  := 'lesser strength potion';
    name[2]  := 'a lesser agility potion';
    name[3]  := 'a refresh potion';
    name[4]  := 'a nightsight potion';
    name[5]  := 'lesser cure potion';
    name[6]  := 'a light heal potion';
    name[7]  := 'lesser poison potion';
    name[8]  := 'a lesser explosion potion';
    name[9]  := 'a agility potion';
    name[10] := 'a strength potion';
    name[11] := 'a poison potion';
    name[12] := 'cure potion';
    name[13] := 'a full refresh potion';
    name[14] := 'a heal potion';
    name[15] := 'a greater agility potion';
    name[16] := 'greater strength potion';
    name[17] := 'a explosion potion';
    name[18] := 'greater cure potion';
    name[19] := 'greater heal potion';
    name[20] := 'a greater poison potion';
    name[21] := 'a invisibility potion';
    name[22] := 'a greater explosion potion';
    name[23] := 'a homeric might potion';
    name[24] := 'a deadly poison potion';
    name[25] := 'a lesser mego invulnerability potion';
    name[26] := 'a grand mage refresh elixir potion';
    name[27] := 'a greater homeric might potion';
    name[28] := 'a Mego Invulnerability potion';
    name[29] := 'a tamla heal potion';
    name[30] := 'a greater Mego Invulnerability potion';
    name[31] := 'a elixer potion';
    name[32] := 'a totem';
    name[33] := 'a tamla cure potion potion';
    name[34] := 'Phandel''s Fine Intellect';
    name[35] := 'Phandel''s Fabulous';
    name[36] := 'Phandel''s Fantastic Intellect';
    name[37] := 'Taint''s Minor Transmutation';
    name[38] := 'Taint''s Major Transmutation';
    name[39] := 'Breath of a dragon';
    name[40] := 'Ice Damnation';
    name[41] := 'Bloodmoss seed';
    for i := 1 to 41 do
    begin
      if name[i] = NeedName then
      begin
        AddToSystemJournal('Что будем лить?: ' + name[i]);
        ConfigNeedReagent(i, UseKeg);
      end;
    end;
  end;

begin
  while True do
  begin
    //if (GetSkillValue('Alchemy') >= 135) then begin AddToSystemJournal('Пятерочка!'); Fulldisconnect; exit;end;
    WaitConnection(5000);
    CheckSave;
    FindDistance := 2;
    SetArStatus(True);
    Hungry(1, -1);
    fulldrop;
    AutoSetup;
  end;
end.
