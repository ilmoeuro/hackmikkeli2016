/*
 * Copyright (C) 2016 Ilmo Euro <ilmo.euro@gmail.com>
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

import com.github.ilmoeuro.hackmikkeli2016.schema.tables.pojos.PlanProposalBase;
import java.util.UUID;

public class PlanProposal extends PlanProposalBase implements Persistable {

    public PlanProposal(
            Integer pk,
            UUID id,
            boolean deleted,
            String title,
            String description,
            String filename,
            UUID planId
    ) {
        super(pk, id, deleted, title, description, filename, planId);
    }
    
    public PlanProposal(
            String title,
            String description,
            String filename,
            Plan plan
    ) {
        this(null, UUID.randomUUID(), false, title, description, filename, plan.getId());
    }
}
