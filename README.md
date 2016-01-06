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


2. その後，Eclipseにインポート
  - （要Eclipse Plug-in Development Environmentなので，Eclipse IDE for Java EE Developersとか）

3. conf.properties.sampleを編集してconf.propertiesにリネーム
  - (その内容の通りにデータベース作成も)

4. 実行すると，EclipseのRun Configurations＞WorkspaceData＞Locationのパスを探索しはじめます
  - conf.propertiesのtargetで指定した名前のJavaプロジェクトをそこにおいておいてください

# 問題点
* 重い・遅い・よく落ちるOR無限ループ
  * 〜10k行くらいが限界
