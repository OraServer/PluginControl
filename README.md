# PluginControl

PluginControl は、他のMinecraftプラグインをゲーム内コマンドから有効化 / 無効化できる管理用プラグインです。

Paper 1.20.4 対応
Java 17 必須

---

## 🔥 機能

* プラグインの有効化
* プラグインの無効化
* プラグイン一覧表示
* プラグイン情報表示
* 無効状態の永続化（再起動後も維持）
* 自動無効化機能（config.yml）

---

## 📦 コマンド

| コマンド                              | 説明           |
| --------------------------------- | ------------ |
| `/plugincontrol enable <plugin>`  | プラグインを有効化    |
| `/plugincontrol disable <plugin>` | プラグインを無効化    |
| `/plugincontrol list`             | 全プラグイン一覧表示   |
| `/plugincontrol info <plugin>`    | プラグイン詳細表示    |
| `/plugincontrol reload`           | config 再読み込み |

---

## 🔑 権限

```
plugincontrol.admin
```

デフォルトでは OP のみ使用可能にすることを推奨します。

---

## ⚙ config.yml

```yaml
auto-disabled:
  - ExamplePlugin
```

### auto-disabled

サーバー起動時に自動で無効化するプラグイン名を指定します。

---

## 🛠 ビルド方法

Gradle を使用します。

```
./gradlew build
```

生成物:

```
build/libs/PluginControl-1.0.0.jar
```

---

## 🚀 動作環境

* Paper 1.20.4
* Java 17

---

## ⚠ 注意事項

* 依存関係があるプラグインを無効化するとエラーが発生する可能性があります。
* 本番環境での使用前にテスト環境で十分に検証してください。
* プラグイン操作はサーバーの動作に影響を与える可能性があります。

---

## 🧠 今後の予定（拡張案）

* 依存関係チェック機能
* GUI 管理画面
* 強制無効化確認システム
* ログ履歴保存
* Web管理連携

---

Created by woxloi
