(ns clojure-course-task03.core-test
  (:use clojure.test
        clojure-course-task03.core))

(group Agent
       proposal -> [person, phone, address, price]
       agents -> [clients_id, proposal_id, agent])


(group Operator
       proposal -> [:all]
       clients -> [:all])

(group Director
       proposal -> [:all]
       clients -> [:all]
       agents -> [:all])

(user Ivanov
      (belongs-to Agent))

(user Sidorov
      (belongs-to Agent))

(user Petrov
      (belongs-to Operator))

(user Directorov
      (belongs-to Operator,
                  Agent,
                  Director))

(deftest ivanov-with-user-test
  (testing "Tesing Ivanov with-user"
    (let [result (with-user Ivanov
                   (select proposal
                           (fields :person, :phone)
                           (where {:price 11})
                           (join agents (= agents.proposal_id proposal.id))
                           (order :f3)
                           (limit 5)
                           (offset 5)))]
      (is (= result "SELECT person,phone FROM proposal  WHERE price = 11 JOIN agents ON agents.proposal_id = proposal.id ORDER BY f3 LIMIT 5 OFFSET 5")))))


(deftest select-agent-agents-test
  (testing "Tesing select-agent-agents"
    (let [result (select-agent-agents)]
      (is (= result "SELECT clients_id,proposal_id,agent FROM agents ")))))


(deftest directorov-with-user-test
  (testing "Tesing Directorov with-user"
    (let [result (with-user Directorov
                   (select clients (fields :all)))]
      (is (= result "SELECT * FROM clients ")))
    (let [result (with-user Directorov
                   (select agents (fields :all)))]
      (is (= result "SELECT * FROM agents ")))))


; Check user that has 2 roles for same table

(group TeamA
       games -> [id, team_a_location])

(group TeamB
       games -> [id, team_b_location])

(user Traitor
      (belongs-to TeamA TeamB))

(deftest traitor-with-user-test
  (testing "Test traitor that has acces to team_a and team_b locations"
    (let [result (with-user Traitor
                    (select games (fields :id :resources :team_a_location :team_b_location)))]
      (is (= result "SELECT id,team_a_location,team_b_location FROM games ")))))
