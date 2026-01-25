declare namespace API {
  type BaseResponseGameInitStateVO = {
    data?: GameInitStateVO
    code?: string
    message?: string
  }

  type BaseResponseJwtResponse = {
    data?: JwtResponse
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

  type InvalidateTokenRequest = {
    userAccount?: string
  }

  type JwtResponse = {
    accessToken?: string
    accessTokenExpire?: number
    refreshToken?: string
    refreshTokenExpire?: number
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

  type TokenRefreshRequest = {
    refreshToken?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserLogOutRequest = {
    userAccount?: string
  }

  type UserRegisterRequest = {
    userName?: string
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }
}
