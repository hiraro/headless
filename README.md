headless
========

# これはなに？
* EclipseJDTのASTParserを使ってJavaソースコードの情報をぶっこ抜く
    * 識別子，コメント，リテラル
    * パッケージ，クラス，メソッド
    * メソッドのコールグラフ的なもの
* MySQLのデータベースにぶっこむ

# 使い方
Eclipseプラグインプロジェクトとして構成されています（たぶん）．

まず，pom.xmlがあるところでターミナルで
    $ mvn install
してください．

その後に，Eclipse（要Eclipse Plug-in Development EnvironmentなのでEclipse IDE for Java EE Developersとか）にインポートしてみてください．

そして，conf.properties.sampleを編集してconf.propertiesにリネームしてください．(その内容の通りにデータベース作成も)

実行すると，EclipseのRun Configurations＞WorkspaceData＞Location内を見に行きます．なので，そこにconf.propertiesのtargetで指定した名前のJavaプロジェクトをおいておいてください．
