package script.db


databaseChangeLog(logicalFilePath: 'field_option.groovy') {

    changeSet(id: '2019-03-29-create-table-field-field-option', author: 'shinan.chenX@gmail.com') {
        createTable(tableName: "field_option") {
            column(name: 'id', type: 'BIGINT UNSIGNED', autoIncrement: true, remarks: 'ID') {
                constraints(primaryKey: true)
            }
            column(name: 'field_id', type: 'BIGINT UNSIGNED', remarks: '字段id') {
                constraints(nullable: false)
            }
            column(name: 'value', type: 'VARCHAR(255)', remarks: '选项值') {
                constraints(nullable: false)
            }
            column(name: 'parent_id', type: 'BIGINT UNSIGNED', remarks: '父选项id')
            column(name: 'sequence', type: 'int', remarks: '排序', defaultValue: "0")
            column(name: 'is_enabled', type: 'TINYINT UNSIGNED(1)', remarks: '是否启用', defaultValue: "1") {
                constraints(nullable: false)
            }
            column(name: 'organization_id', type: 'BIGINT UNSIGNED', remarks: '组织id')

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
        createIndex(tableName: "field_option", indexName: "pk_id") {
            column(name: 'id', type: 'BIGINT UNSIGNED')
        }
        createIndex(tableName: "field_option", indexName: "idx_field_id") {
            column(name: "field_id", type: "BIGINT UNSIGNED")
        }
        createIndex(tableName: "field_option", indexName: "idx_parent_id") {
            column(name: "parent_id", type: "BIGINT UNSIGNED")
        }
        createIndex(tableName: "field_option", indexName: "idx_is_enabled") {
            column(name: "is_enabled", type: "TINYINT UNSIGNED(1)")
        }
        createIndex(tableName: "field_option", indexName: "idx_organization_id") {
            column(name: "organization_id", type: "BIGINT UNSIGNED")
        }
    }

}