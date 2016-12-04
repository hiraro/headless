headless
========

# これはなに？
* EclipseJDTのASTParserを使って指定したEclipseのJavaプロジェクトにあるソースコードの情報をぶっこ抜く
    * 識別子，コメント，リテラル
    * パッケージ，クラス，メソッド
    * メソッドのコールグラフ的なもの
* MySQLのデータベースにぶっこむ

# 使い方
Eclipseプラグインプロジェクトとして構成されています．

1. pom.xmlがあるパスにて

    $ mvn install

2. Eclipseにインポート
  - 要: Eclipse Plug-in Development Environment とか m2e とか Eclipse JDT Plug-in Developer Resources とか Eclipse Java development tools など	
3. conf.properties.sampleを編集してconf.propertiesにリネーム
  - データベース作成しておく
  
4. net.umanohone.headless.application を 実行すると Eclipse の `Run Configurations＞WorkspaceData＞Location`のパスを探索しはじめます
  - conf.propertiesのtargetで指定した名前のJavaプロジェクトをそこにおいておいてください

# 問題点
* 重い・遅い・よく落ちるOR無限ループ
  * 〜10k行くらいが限界
