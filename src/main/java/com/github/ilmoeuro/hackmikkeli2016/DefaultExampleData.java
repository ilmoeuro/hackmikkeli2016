/*
 * Copyright (C) 2015 Ilmo Euro <ilmo.euro@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.ilmoeuro.hackmikkeli2016;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

@RequiredArgsConstructor
public final class
    DefaultExampleData
implements
    ExampleData
{
    @Override
    public void populate(DSLContext jooq) {
        UnitOfWork uw = new UnitOfWork(jooq);

        Plan plan = new Plan(
                "Plan 1",
                "Tähän asemakaavan kuvaus",
                600,
                600,
                80);
        PlanProposal proposal1 = new PlanProposal(
                "Draft 1",
                "Tähän luonnoksen 1 kuvaus",
                "example1.png",
                plan);
        PlanProposal proposal2 = new PlanProposal(
                "Draft 2",
                "Tähän luonnoksen 2 kuvaus",
                "example2.png",
                plan);
        PlanProposal proposal3 = new PlanProposal(
                "Draft 3",
                "Tähän luonnoksen 3 kuvaus",
                "example3.png",
                plan);
        PlanProposal proposal4 = new PlanProposal(
                "Draft 4",
                "Tähän luonnoksen 4 kuvaus",
                "example4.png",
                plan);

        uw.addEntity(plan);
        uw.addEntity(proposal1);
        uw.addEntity(proposal2);
        uw.addEntity(proposal3);
        uw.addEntity(proposal4);

        uw.execute();
    }

}
