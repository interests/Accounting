Приложение по учету расходов.

Представляет из себя базу данных SQLite, в которой фиксируются записи по расходу денежных средств.
Используется 2 таблицы:
Таблица категорий (categories), в которой указываются категории расхода денежных средств (еда, авто, телефон, ...), 
для каждой категории имеется возможность указать собственную иконку (из списка предопределенных иконок)

Таблица записей (records), в которой непосредственно фиксируются операции расхода денежных средств.

Записи по учету расходов группируются по дням, с отображением итогов по расходу денежных средств за день.

При добавлении записи открывается диалог для последовательного выбора категории, суммы расхода и описания.
Дата расхода автоматически устанавливается в текущую дату, т.к. предполагается, что расходы будут заноситься
оперативно.

При необходимости, можно открыть карточку записи, внести в нее изменения (поменять дату, категорию, сумму, описание) 
и сохранить (нажав на кнопку "Сохранить").

Для того, чтобы удалить запись, необходимо выполнить длительное нажатие на строчке, которую требуется удалить и 
подтвердить удаление строки.