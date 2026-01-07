# 今日AI新闻聚合APP（Android）

- 最低Android版本：8.0 (API 26)
- 技术栈：Kotlin、Room、WorkManager、OkHttp、Media3、TextToSpeech
- 主要功能：多源RSS聚合、去重、摘要、文字+语音播报、手动刷新、离线支持

## 目录结构

- app/src/main/java/com/airss
  - MainActivity.kt：单Activity入口，加载HomeFragment
  - AppGraph.kt：简单DI，提供Repository实例
- app/src/main/java/com/airss/data
  - Repository.kt：抓取→去重→摘要→入库→流式订阅
  - db/：Room实体与DAO
  - rss/：默认源与XmlPullParser解析
- app/src/main/java/com/airss/domain
  - Deduper.kt：SimHash简化版
  - Summarizer.kt：TextRank占位实现
  - CloudSummarizer.kt：OpenAI兼容API占位
  - SettingsManager.kt：SharedPreferences配置存取
  - CombinedBriefBuilder.kt：合并简报生成器
- app/src/main/java/com/airss/ui
  - home/：首页列表与刷新
  - player/：播报页（TTS基础）
  - settings/：设置页骨架
  - sources/：数据源管理骨架
- app/src/main/java/com/airss/work
  - RefreshWorker.kt：定时抓取骨架

## 构建
- Android Studio：打开项目，等待Gradle同步，Run/Build生成APK
- Gradle命令：`./gradlew assembleRelease`

## 默认RSS源
- 国际：OpenAI、Hugging Face、DeepMind、arXiv（cs.AI/cs.LG）、MIT News
- 中文：雷锋网、量子位、36氪、钛媒体
