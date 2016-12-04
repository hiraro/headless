headless
========

# これはなに？
* EclipseJDTのASTParserを使って指定したEclipseのJavaプロジェクトにあるソースコードの情報をぶっこ抜く
    * 識別子，コメント，リテラル
    * パッケージ，クラス，メソッド
    * メソッドのコールグラフ的なもの
* MySQLのデータベースにぶっこむ

# 使い方
Eclipseプラグインプロジェクトとして構成されています (maven使用)．

1. pom.xml があるパスにて `mvn install`

2. Eclipse にインポート
    
    - 要Eclipseプラグインインストール
       - Eclipse Plug-in Development Environment とか m2e とか Eclipse JDT Plug-in Developer Resources とか Eclipse Java development tools など	

3. conf.properties.sample を編集して conf.properties へリネーム
   
   - 指定したデータベース等作成しておく
  
4. net.umanohone.headless.application を 実行する

   - Eclipse の `Run Configurations＞WorkspaceData＞Location`のパスを探索しはじめます
      - conf.properties の target で指定した名前の Javaプロジェクト を、予めそこにおいておいてください

# 問題点
* 重い・遅い・よく落ちるOR無限ループ
  * 〜10k行くらいが限界
