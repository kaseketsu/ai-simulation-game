// tailwind 拓展配置
module.exports = {
  content: [
    "./src/**/*.{html,js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        // 思源宋体（衬线，典雅）
        'source-serif': ['思源宋体', 'Source Han Serif', 'Noto Serif CJK SC', 'SimSun', 'serif'],
        // 思源黑体（无衬线，通用）
        'source-sans': ['思源黑体', 'Source Han Sans', 'Noto Sans CJK SC', 'Microsoft YaHei', 'sans-serif'],
        // 华光俊秀体（通用，刚柔并济）
        'huaguang': ['华光俊秀体', 'SimHei', 'Microsoft YaHei', 'sans-serif'],
        // 字体圈欣意吉祥宋（古风圆润）
        'jixiang-song': ['字体圈欣意吉祥宋', 'SimSun', 'STSong', 'serif'],
        // 香萃刻宋（雕刻宋体，古朴）
        'xiangcui-song': ['香萃刻宋', 'SimSun', 'STSong', 'serif'],
        // 杨任东竹石体（手写楷体，文艺）
        'zhushi': ['杨任东竹石体', 'KaiTi', 'STKaiti', 'serif'],
        // 庞门正道标题体（粗体醒目）
        'pangmen': ['庞门正道标题体', 'SimHei', 'Microsoft YaHei', 'bold', 'sans-serif'],
        // HarmonyOS Sans（鸿蒙字体，现代科技）
        'harmony': ['HarmonyOS Sans', 'Source Han Sans', 'Noto Sans CJK SC', 'sans-serif'],
      },
    },
  },
  plugins: []
}
