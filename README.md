# Goal-based-agent
Creation of an agent with the ability to move freely around a map and interact with the environment. 

In particular, the agent can:
- Detect food (represented as points on the map) and go after it to feed.
- Detect walls and map boundaries and act accordingly.
- Detect other agents and go after them by shooting them.
- Dodge enemy bullets.
- When there is little time left in the game, all shields will be activated so that no other agent can shoot you.

This practice aims to describe and program the behavior of an intelligent agent that, in order to perform all its functions and survive, must take as much food as possible (which will give it the necessary strength). The metabolism of the agent is very fast and very delicate. That it is fast means that it is continuously consuming the food and that it is delicate means that it can only digest a certain type of food because it is allergic to the foods of the other agents. If you touch a type of food that is not suitable it will negatively affect your health.

## Environment

The environment will be a closed enclosure with randomly generated obstacles and in which there may also be other agents.
In the environment, in addition to other agents, you can find different objects identified by their colors:

- Objects of the same color as the agent represent that agent's food, the basic resource that can feed it.
- Objects of a different color than the agent represent the food of another agent and that the first agent cannot digest and affects it negatively.
- The white objects are protective shields, which if activated prevent contact with another object and protect the agent from any contact.

## Goal

Agent's mission will be to find and collect the right foods for its metabolism and avoid those it cannot digest properly.
Therefore, in this practice the behavior of an agent must be programmed with the aim of taking and preserving, with a limited time, the greatest possible number of food (resources), using all the means at his disposal , and to avoid touching objects that could hurt him (any other type of food).

## The agent

The agent can obtain information from the environment through its sensors and can act through its actuators.
Morphologically, agents are 25x25 pixel squares and have:

- Mechanisms of displacement and rotation with variable speeds that allow the agent to move around the environment.
- A system of three configurable viewers that are the only tool to see the environment.
- A device that allows travel to hyperspace, which makes the agent disappear and return
to appear in another random place.
- A defense mechanism or shield, which if activated allows the agent to be isolated from anyone
other object in the environment.
- A limited range food launch mechanism.
- The agent's physiology can only withstand the impact of 5 throws.

It should be taken into account that:
- If you throw food at another agent and do it, it causes an allergy to the wounded person and he needs food to heal (he loses a food resource and part of his health).
- If an agent runs out of food, it cannot use its effectors (neither movement nor throwing).
- To activate a shield you must have one (in the initial configuration or picked up from the environment).
- To activate hyperspace you must have it (in the initial configuration).


The agent can be understood as follows:




<img width="487" alt="Captura de pantalla 2023-11-20 a las 17 52 41" src="https://github.com/maribel95/Goal-based-agent/assets/61268027/3dd267f3-126d-4ef1-81e3-6726e72e7ded">

















