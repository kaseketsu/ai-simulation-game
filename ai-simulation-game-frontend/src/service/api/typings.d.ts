declare namespace API {
  type BaseResponseGameInitStateVO = {
    data?: GameInitStateVO;
    code?: string;
    message?: string;
  };

  type BaseResponseLoginUserVO = {
    data?: LoginUserVO;
    code?: string;
    message?: string;
  };

  type BaseResponsePageVOSpiritualMaterialAllCat = {
    data?: PageVOSpiritualMaterialAllCat;
    code?: string;
    message?: string;
  };

  type BaseResponsePlayProgressVO = {
    data?: PlayProgressVO;
    code?: string;
    message?: string;
  };

  type BaseResponseString = {
    data?: string;
    code?: string;
    message?: string;
  };

  type BaseSpiritualMaterialsAddReq = {
    name?: string;
    type?: number;
    rarity?: number;
    url?: string;
    price?: number;
    storeType?: number;
    materialGrade?: number;
  };

  type DailyInfoComputeRequest = {
    openDays?: number;
  };

  type GameInitRequest = {
    userId?: number;
    userName?: string;
    userRole?: string;
    storeType?: string;
    storeName?: string;
    sense?: number;
    speakingSkill?: number;
    cookingSkill?: number;
  };

  type GameInitStateVO = {
    userId?: number;
    storeType?: string;
    sense?: number;
    speakingSkill?: number;
    cookingSkill?: number;
    openDays?: number;
    timePeriod?: string;
    earnedMoney?: number;
    storeLevel?: number;
  };

  type LevelUpRequest = {
    userId?: number;
    levelUpCount?: number;
    senseUp?: number;
    speakingSkillUp?: number;
    cookingSkillUp?: number;
  };

  type LoginUserVO = {
    userId?: number;
    userName?: string;
    userAccount?: string;
    userAvatar?: string;
    userRole?: string;
    userState?: number;
  };

  type PageVOSpiritualMaterialAllCat = {
    currentPage?: number;
    pageSize?: number;
    total?: number;
    pages?: number;
    records?: SpiritualMaterialAllCat[];
  };

  type PlayProgressQueryRequest = {
    userId?: number;
  };

  type PlayProgressSaveRequest = {
    userId?: number;
    userName?: string;
    storeType?: string;
    sense?: number;
    speakingSkill?: number;
    cookingSkill?: number;
    openDays?: number;
    timePeriod?: string;
    earnedMoney?: number;
    storeLevel?: number;
  };

  type PlayProgressVO = {
    openDays?: number;
    storeName?: string;
    timePeriod?: string;
    earnedMoney?: number;
    storeLevel?: number;
    userName?: string;
    storeType?: string;
    sense?: number;
    speakingSkill?: number;
    cookingSkill?: number;
    userId?: number;
  };

  type RoleAddRequest = {
    roleName?: string;
    roleCode?: string;
    roleDesc?: string;
  };

  type SpiritualMaterialAllCat = {
    type?: number;
    normalName?: string;
    normalUrl?: string;
    normalPrice?: number;
    rareName?: string;
    rareUrl?: string;
    rarePrice?: number;
    superRareName?: string;
    superRareUrl?: string;
    superRarePrice?: number;
    mythicalName?: string;
    mythicalUrl?: string;
    mythicalPrice?: number;
  };

  type SpiritualMaterialBaseRequest = {
    currentPage?: number;
    pageSize?: number;
    type?: number;
  };

  type SysCodeAddRequest = {
    codeCategory?: string;
    codeKey?: string;
    codeValue?: string;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserRegisterRequest = {
    userName?: string;
    userAccount?: string;
    userPassword?: string;
    checkPassword?: string;
  };
}
