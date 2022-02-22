# Agenda week 3.3

Date: 22.02.2022

Main focus: This week's tasks/HCI draft

Chair: Rafael Petouris

Note taker: Vlad Nitu



## Opening 

- Check if everyone is present

## Points of action

- Teamwork 3A and 3B -> Deadline on Friday
- HCI Report Draft -> Deadline on Friday
- Information Literacy assignment -> Deadline on Sunday
- Code of Conduct feedback
- Backlog feedback -> Compare to sample backlog to see if we have everything
- API endpoints
- Good GitLab practices
- Choose the next chairman and secretary


## Questions for the TA

- Feedback for template?
- Where to put activities (for the quizzz)?

## Question round

- If anyone has any questions address them now


## Closing

- Good job this previous week!

***

# Minutes week 3.3


## Plans for this week

- Schedule another meeting before the “official” meeting on Thursday in order to prepare the mock up for the game.
- Agreed on the team we will group with to evaluate each others HCI assignments 
- Have the HCI mock-up finished by Friday’s deadline (preferable on Tuesday so that we can ask our TA for feedback) and also do the Assignments 3A and 3B (which are both team assignments).
- Add check style after agreeing as a team on some basic rules regarding coding standards (indentation, spacing, etc.) . -> between 5 and 10 rules.
- Enrol to CSE Information Literacy 1 course and go through BS exercises provided for this course. Note that this is a mandatory assignment, deadlined on Sunday (27th Feb.)
- We will compare our Backlog to the official one (when it is released) and check whether we’ve missed any issue. If so, we will update our issues so that they will be similar to the official document.
- Decide on the implementation on propagating data (polling, long-polling or WebSockets). 

## Reflection on last week

- Installed the required software and tested it on the repository-template to make sure everything works fine.
- We chose the next chairman (Rafael) and secretary (Vlad).
- Had an additional meeting to update issues on GitLab.
- We met all deadlines scheduled for last Friday (18.02.2022): posted our Backlog issues on GitLab (by adhering to TA’s tips, such as: describing each issue, adding relevant labels to each issue, making sure for completeness and consistency), done Assignment 1A individually (by reading the indicated chapters on BS) and Assignment 2B (Code of Conduct) in team.
- Participated actively as a team to the guest lecture regarding Human-Computer Interaction (HCI), after our additional meeting where we re-checked our weekly assignments, we attended online the Client Q&A part 2 lecture as a team, discussing the new issues we should address (after our client clarified some requests) and noted some relevant responses of Sebastian to other groups’ questions. 
- Discussed with our TA regarding our previous work to make sure we are on track as a group. 
- We need to find another team to group with in order to evaluate their HCI assignment.


## Backlog feedback

### PROS:
- Same structure for user stories
- Definition of Done in description
- Lock for issues (from 1 to 3) 
- Mentioning issues (i.e: #32)
- Proper weights, labels 
### CONS:
- No explicit reason for some issues.


## Code Of Conduct feedback
 
### PROS:
- Almost good. Missing some points.
### CONS:
#### Product: 
- more specific with deliverables (assignments, HCI document, assignment presentation) .
- clearly state the assignments
- other contribution standards decided by the team -> talk more

#### Planning: 
- how/when going to decide who is responsible for each task issue. 
- chairman  responsible for the work (?)
- how much power given to the chairman (if someone does not do his part, what does the chairman do?) 

#### Behvaiour:
- disagreements -> try to be specific (concrete examples and solutions). 
- what we expect from the TA.
- Consequences: should help a person to do better next time, NOT SEEN as a punishment. 
- Negative feedback: not a criticism, meant to help you

#### Commitment: (redo completely)
- Re-check: checkstyle, complexity of methods, code reviews, etc. 
- Chairman/Secretary:  did their parts in a right manner. 

#### Meeting: (needs to be more specific) 
- Decide on AT LEAST 1 specific day for meetings (i.e: Thursday). 
- What is the goal of the meeting (SPECIFIC, i.e: creating MR, review X).
- How much time before we schedule a meeting (i.e: 3 days in advance). 
		 
#### Decision making: 
- What if someone does not want to comply after voting ?
#### Consequences: 
- Too vague, write AT LEAST 2 concrete consequences.




## Meeting discussion
- [16.45] **Meeting started by chairman**
- [16.46] **TA announces deadlines** - {also some tips about API endpoints} 
- [16.49] **Chairman leads the meeting** -{Talks about important points to touch} 
- [16.50] **TA ends giving feedback on Code of Conduct** - { } 
- [17.08] **Stand-up meeting** - {Every member presents his work conducted during last week.} 
- [17.12] **TA expectation regarding every member being equally active** - {} 
- [17.14] **Ask for feedback regarding quizzz prototype** - {
- Realistic design. 
- Don’t spend a lot of time on design (it is not graded)
- SHOULD BE FUNCTIONAL.
- Take screenshots of app.moqups.com project. -> before and after feedback from the other team.
- Set expectations from the other team (i.e: 7-10 points of feedback).  Ask them to follow guidelines from HCI lectures. 
} 
- [17.18] **Talk about Information Literacy assignment** - {Get a PASS grade after doing all levels.} 
- [17.20] **API endpoints examples from TA** - {
- Show examples of API endpoints usage.
- We basically start our project by working over repository-template.
- Use findAll(), findById() by using classes that extend JPARepository. 
}
- [17.27] **GITLAB good practice discussion** - {
- 1 issue means 1 feature. 
- Make them as small as possible (i.e: such that 1/2 people can finish it in a couple of hours).
#### TIP:
    - Create boards(i.e: % ”Sprint 1” milestones) for each sprint, where we decide for each sprint what features are going to be implemented + some new labels: i.e: status::unassigned, status::scheduled, status::in-progress, status::on-review, status::completed.
- DO NOT approve own MR.
- Before approval check for pipeline pass. 
- Comment on GITLAB when reviewing. 
} 
- [17.37] **Rotate each time with other teams of our TA** - {Next Thursday: 29.02.2022 meeting is scheduled at 13.45.} 
- [17.39] **Meeting ends** - {Chairman closes.} 