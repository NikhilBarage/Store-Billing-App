import sqlite3

connection = sqlite3.connect("store.db")
cursor = connection.cursor()

while True:
    query = input("SQL> ")

    if query.lower() == "exit":
        break

    try:
        cursor.execute(query)

        if query.strip().lower().startswith("select"):
            rows = cursor.fetchall()

            for row in rows:
                print(row)
        else:
            connection.commit()
            print("Query Executed Successfully.")

    except Exception as e:
        print(e)

connection.close()