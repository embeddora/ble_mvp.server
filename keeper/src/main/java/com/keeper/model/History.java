
package com.keeper.model;

import java.io.Serializable;

public class History implements Serializable
{
	public int id;
	public String name;
	public String state;
	public long time;

	public History(int id, String name, String state, long time)
	{
		this.id = id;
		this.name = name;
		this.state = state;
		this.time = time;
	}

}
