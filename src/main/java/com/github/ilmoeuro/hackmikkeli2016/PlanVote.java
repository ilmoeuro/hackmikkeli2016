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

import com.github.ilmoeuro.hackmikkeli2016.schema.tables.pojos.PlanVoteBase;
import java.util.UUID;

public class PlanVote extends PlanVoteBase implements Persistable {

    public PlanVote(
            Integer pk,
            UUID id,
            boolean deleted,
            UUID proposalId,
            int tileNumber
    ) {
        super(pk, id, deleted, proposalId, tileNumber);
    }
    
    public PlanVote(
            PlanProposal proposal,
            int tileNumber
    ) {
        this(null,
                UUID.randomUUID(),
                false,
                proposal.getId(),
                tileNumber);
    }
    
}
