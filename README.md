# KnapsackProblem
Knapsack problem solved with BB variation

# Usage 

To run with jar you need to use the following command
<pre>
...> java -jar -Dfile.encoding=UTF-8 KnapsackProblem.jar "d:/path/sampleInput.txt"
</pre>


The expected output for testing file is 


![Output](output.JPG)

Syntax errors are displayed in red on console (error stream).
When application is starting from .class it does not need any encoding parameter. 

# Problem Analysis 

The packing problem defined in Problem Description can be considered as a variant of Knapsack Problem which is very commonly known 
Discrete Optimization Problem. The variant considered here is known as Bounded 0-1 Knapsack Problem (because item can be presented maximum once in a solution).
The decision problem form of the knapsack problem is NP-complete, so the naive approach with exhaustive search is undesirable 
for large number of variables. The solution can be by:
- application of greedy search algorithms (Sequential Forward, Backward, Plus-Minus, Bidirectional, Beam Search, etc.),
- application of exhaustive search strategy.

## Problem formulation
Given a set of n items numbered from 1 up to n, each with a weight w_i and a cost value v_i, along with a maximum weight capacity W:

maximize: ![Formula](85620037d368d2136fb3361702df6a489416931b.svg)


subject to: ![Formula](dd6e7c9bca4397980976ea6d19237500ce3b8176.svg)


where: x_i is binary {0,1} variable, and i is in {1,2,3,...,c} and sum of x_i <15.




The additional constraints, ie.  maximum cost of item<100  and maximum weight of item <100 do not affect on the solution of optimization problem, because items can be removed from list of candidates for solution.

## Solution with Branch and Bound
Since we can assume monotonicity assumption (addition of items can only increase both total cost and total weight, because they are just summing up and nonnegative), then Branch and Bound Strategy can be applied effectively. 
B&B discovers branches within the complete search space by using estimated bounds to limit the number of possible solutions. The different types (FIFO, LIFO, LC) define different 'strategies' to explore the search space and generate branches.

* FIFO (first in, first out): always the oldest node in the queue is used to extend the branch. This leads to a breadth-first search, where all nodes at depth d are visted first, before any nodes at depth d+1 are visited.

* LIFO (last in, first out): always the youngest node in the queue is used to extend the branch. This leads to a depth-first search, where the branch is extended through every 1st child discovered at a certain depth, until a leaf node is reached.

* LC (lowest cost): the branch is extended by the node which adds the lowest additional costs, according to a given cost function. The strategy of traversing the search space is therefore defined by the cost function.


I decided  to use  the least cost(LC) variant as it uses Heuristic Cost Function. 
As 0/1 Knapsack is about maximizing the total value, and BB algorithm is about minimalization, then the problem needs to be converted by taking negative of the given values. 

The implemented algorithm works as follows:

 1. Filter out items which are too heavy or too costly (>100)

 2. Sort items based on cost/weight ratio

 3. Insert empty node to PriorityQueue

 4. Iteratively perform B&B with LC 

```mermaid
sequenceDiagram
SkillRouter->>ContainerPlaySkillMap: /playskill/map/:id
ContainerPlaySkillMap->>PlaySkillMap: call
PlaySkillMap->>Map API: GET /map/configuration/:id
Map API-->>PlaySkillMap: mapConfiguration document

loop get all geojsons
PlaySkillMap->>Map API: GET /map/geojson/:id
Map API-->>PlaySkillMap: geojson document
end
Note right of PlaySkillMap: Get all static geojsons in loop
PlaySkillMap->>SkillMap API: GET /skill/{skillId}/site 
SkillMap API-->>PlaySkillMap: sites document
PlaySkillMap-->GeoAction: update sites layer
loop contribute sample 
PlaySkillMap-->Workflow API: POST /submit/case???
PlaySkillMap->>SkillMap API: GET /skill/{skillId}/site 
SkillMap API-->>PlaySkillMap: sites document
PlaySkillMap-->GeoAction: update sites layer
end
Note right of Workflow API: Update sites after each conttriution
```

# Skills with map (API)


<details>
 <summary><code>GET</code> <code><b>/api/event</b></code>  and <code><b>/api/event/{eventId}</b></code> </summary>

Already exist. Now the endpoint are optionally returning (if the type is explicitly defined in the skill doc) a type for the skills in the skillsAvailable array. For instance : 

    [
        {
            ...
            "skillsAvailable": [
                {
                    "livePresenterExperimentId": "15f95f00-b0e6-4712-9af8-b5cdd0678279"
                    "miniWorkflowSetId": "e7a2b5c5-4543-43eb-964c-4029ede68157",
                    "miniWorkflowKey": "mw1",
                    "presentationId": "6a0feae5-22d1-446a-aa23-c4e715b1bce6",
                    "uuid": "3f93e4c5-1340-4780-a787-4c59f8e0a9e3",
                    "description": "Birds identification : Certification"
                },
                {
                    "livePresenterExperimentId": "cc8ee452-7fa4-4ccd-bb67-14f6923f1af6",
                    "type": "map",
                    "miniWorkflowSetId": "b6c8c461-4a0e-40a3-99b7-59e0f9b33391",
                    "miniWorkflowKey": "mw1",
                    "presentationId": "50492b58-d8b8-4ea0-b6d7-f376a09a566a",
                    "uuid": "638fc26a-66fd-415e-88bc-4278245812f2",
                    "description": "Birds identification : Certification (vertical version)"
                }
            ]
        }
    ]

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`        | `{"map": [{"siteId": "uuid", "numberOfCases": 2]}`                                |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |

</details>

http://localhost:3000/api/event

## Get list of centers

<details>
 <summary><code>GET</code> <code><b>/skill/{skillId}/site</b></code> </summary>

Only compatible with skills having the type "map".

##### Parameters

> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | skillId      |  required | string  | The skill UUID  |


##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`        | `{"map": [ {"siteId": "uuid", "cases": [{"key": "case1", "available": true}]}]}`                                |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |

</details>

## Get a center (needed?)

<details>
 <summary><code>GET</code> <code><b>/skill/{skillId}/site/{siteId}</b></code> </summary>

Only compatible with skills having the type "map".

##### Parameters

> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | skillId      |  required | string  | The skill UUID  |


##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`        | `{"siteId": "uuid", "cases": [{"key": "case1", "available": true}]}`                                |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |

</details>

## Get the current workload or trigger the assignation of one case from one center

<details>
 <summary><code>GET</code> <code><b>/skill/{skillId}/workload</b></code></summary>

Only compatible with skills having the type “map”. This endpoint returns a case (if no case is already assigned, the query parameter **siteId** must be provided in order to assign a case to the user from the site selected). The format of the response is the same than the /workload API used for the "playskill". First implementation is only compatible with the new skill type (map).

##### Parameters

> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | skillId      |  required | string  | The skill UUID  |

##### Query parameters

> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | siteId      |  optional | string   | The site UUID. When not passed the API returns a case if one is already assigned, otherwise no case is returned  |


##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`        | `{"currentCase": "...", "expId": "...", listOfCases: [...], "miniWorkflow": {...} }` |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |                                                      

</details>
