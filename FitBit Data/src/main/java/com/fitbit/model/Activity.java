package com.fitbit.model;

import java.util.List;
import com.fitbit.model.LifetimeActivities;

public class Activity {

    private LifetimeActivities lifetime;
    private DailyActivities summary;
    private List<Activities> activities=null;
    public List<Activities> getActivities() {
		return activities;
	}

	public void setActivities(List<Activities> activities) {
		this.activities = activities;
	}

	public DailyActivities getSummary() {
		return summary;
	}

	public void setSummary(DailyActivities summary) {
		this.summary = summary;
	}

	public LifetimeActivities getLifetime() {
        return lifetime;
    }

    public void setLifetime(LifetimeActivities lifetime) {
        this.lifetime = lifetime;
    }
}
