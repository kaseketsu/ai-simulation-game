declare namespace API {
  type BaseResponseGameInitStateVO = {
    data?: GameInitStateVO
    code?: string
    message?: string
  }

  type BaseResponseLoginUserVO = {
    data?: LoginUserVO
    code?: string
    message?: string
  }

  type BaseResponsePlayProgressVO = {
    data?: PlayProgressVO
    code?: string
    message?: string
  }

  type BaseResponseString = {
    data?: string
    code?: string
    message?: string
  }

  type GameInitRequest = {
    userId?: number
    userName?: string
    userRole?: string
    storeType?: string
    storeName?: string
    sense?: number
    speakingSkill?: number
    cookingSkill?: number
  }

  type GameInitStateVO = {
    userId?: number
    storeType?: string
    sense?: number
    speakingSkill?: number
    cookingSkill?: number
    openDays?: number
    timePeriod?: string
    earnedMoney?: number
    storeLevel?: number
  }

  type LoginUserVO = {
    userName?: string
    userAccount?: string
    userAvatar?: string
    userState?: number
  }

  type PlayProgressQueryRequest = {
    userId?: number
  }

  type PlayProgressSaveRequest = {
    userId?: number
    userName?: string
    storeType?: string
    sense?: number
    speakingSkill?: number
    cookingSkill?: number
    openDays?: number
    timePeriod?: string
    earnedMoney?: number
    storeLevel?: number
  }

  type PlayProgressVO = {
    openDays?: number
    storeName?: string
    timePeriod?: string
    earnedMoney?: number
    storeLevel?: number
    userName?: string
    storeType?: string
    sense?: number
    speakingSkill?: number
    cookingSkill?: number
    userId?: number
  }

  type RoleAddRequest = {
    roleName?: string
    roleCode?: string
    roleDesc?: string
  }

  type SysCodeAddRequest = {
    codeCategory?: string
    codeKey?: string
    codeValue?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserRegisterRequest = {
    userName?: string
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }
}
